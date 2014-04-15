/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.mp.tasking;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.exceptions.SubmitFault;
import net.eads.astrium.hmas.util.Algorithms;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.util.structures.tasking.enums.RequestType;
import net.eads.astrium.hmas.util.structures.tasking.enums.TaskHandlerType;

/**
 *
 * @author re-sulrich
 */
public class SensorFeasibilityPlanningServiceHandler extends Thread {

    public enum ExecutionType {

        allSynchronous, planningSynchronous, allAsynchronous
    }
    public static Map<String, SensorFeasibilityPlanningServiceHandler> currentTasks;
    public static int nbProducts = 0;
    public static WaitForTerminationThread wftThread = startWFTThread();

    private static WaitForTerminationThread startWFTThread() {
        WaitForTerminationThread t = new WaitForTerminationThread();
        t.start();
        return t;
    }
    public boolean isCancelled;
    public boolean isComplete;
    public final ExecutionType executionType;
    private String sensorFeasibilityTaskId;
    private String sensorPlanningTaskId;
    private boolean isThreadRunFinished;
    private Calendar estimatedTOC;
    private int percentCompletion;
    private int nbComplete;
    private List<Segment> segments;
    private List<Segment> accepted;
    private List<Segment> acquired;
    private final SensorPlanningHandler planningDBHandler;
    private Status status;

    public String getSensorFeasibilityTaskId() {
        return sensorFeasibilityTaskId;
    }

    public String getSensorPlanningTaskId() {
        return sensorPlanningTaskId;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public Status getStatus() {
        return status;
    }

    public SensorFeasibilityPlanningServiceHandler(
            ExecutionType executionType,
            String sensorFeasibilityTaskId,
            List<Segment> segments,
            SensorPlanningHandler dbHandler,
            String mmfasTaskID) throws SQLException, ParseException {
        super();

        isCancelled = false;
        this.executionType = executionType;
        this.sensorFeasibilityTaskId = sensorFeasibilityTaskId;
        this.segments = segments;
        this.planningDBHandler = dbHandler;

        this.sensorPlanningTaskId =
                dbHandler.createPlanningTaskFromFeasibility(sensorFeasibilityTaskId, mmfasTaskID);

        dbHandler.createRequest(TaskHandlerType.sensor, sensorPlanningTaskId, RequestType.submitSegmentByID);

        status = this.planningDBHandler.getStatus(sensorPlanningTaskId);

        for (Segment segment : segments) {
            dbHandler.setSegmentPlanningTask(segment.getSegmentId(), sensorPlanningTaskId);
        }
    }

    public void runSynchronousPartThenStart() throws SQLException, ParseException, SubmitFault {
        if (!executionType.equals(ExecutionType.allAsynchronous)) {
            this.submitPlanning();
        }

        if (isCancelled) {
            return;
        }

        if (executionType.equals(ExecutionType.allSynchronous)) {
            this.waitForAcquisitions();
        } else {
            this.start();
        }
    }

    @Override
    public void interrupt() {

        System.out.println("Interrupting...");

        if (!isComplete) {
            isCancelled = true;
        }

        if (this.executionType.equals(ExecutionType.allSynchronous)) {
            if (this.getState().equals(State.TERMINATED)) {
                System.out.println("Thread is already finished");
            } else {
                //If the thread is still running, it might be in a sleeping mode
                //So interrupt it
                if (this.getState().equals(State.WAITING)
                        || this.getState().equals(State.BLOCKED)
                        || this.getState().equals(State.TIMED_WAITING)) {
                    super.interrupt();

                    while (!isThreadRunFinished) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
        }
        //Check all the acquisitions, which will put the cancellable segments to CANCELLED status
        //And the task to CANCELLED if at least one segment has been cancelled
        try {
            this.checkAcquisitions();
        } catch (SQLException ex) {
            System.out.println("Error while cancelling task.");
            ex.printStackTrace();
            Logger.getLogger(SensorFeasibilityPlanningServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Interrupted : state is " + this.getState() + "\nisCancelled ? " + isCancelled);
    }

    @Override
    public void run() {

        isThreadRunFinished = false;
        //Adding the current task to the currently executed tasks
        System.out.println("Adding to current tasks : " + sensorPlanningTaskId);
        if (currentTasks == null) {
            currentTasks = new HashMap<>();
        }
        currentTasks.put(sensorPlanningTaskId, this);

        try {
            //In other cases, this part has already been executed
            if (executionType.equals(ExecutionType.allAsynchronous)) {
                submitPlanning();
            }

            if (executionType.equals(ExecutionType.allSynchronous)) {
                waitForAcquisitions();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (SubmitFault ex) {
            ex.printStackTrace();
        }

        //Removing the current task, which is now finished, from the current tasks
        //In the other modes, the WaitForTermination thread will do the rest
        if (executionType.equals(ExecutionType.allSynchronous)) {
            System.out.println("Removing from current tasks : " + sensorPlanningTaskId);
            currentTasks.remove(sensorPlanningTaskId);
        }

        isThreadRunFinished = true;
    }

    /**
     * Fake submit segments from a Feasibility. Algorithm is : for each segment
     * of the feasibility (parameter passed, segments are sorted in
     * SubmitSegmentByIDOperation.validRequest) wait 30 seconds getRandomPercent
     * if percent > 10 set segment=planned else set segment=rejected
     *
     * Task = InExecution wait getRandomPercent if percent > 1 complete else
     * failed
     *
     * @throws SQLException
     */
    /**
     * This function plans the askeds segments and returns three status for each
     * of them : "ACCEPTED", "REJECTED", "CANCELLED"(if Cancel has been called)
     * It fills the <accepted> field with only the "ACCEPTED" segments
     *
     * @throws SQLException
     * @throws ParseException
     * @throws SubmitFault
     */
    public void submitPlanning() throws SQLException, ParseException, SubmitFault {

        accepted = new ArrayList<>();
        percentCompletion = 0;
        estimatedTOC = Calendar.getInstance();

        System.out.println("Starting task " + this.sensorPlanningTaskId + " "
                + "from feasibility " + this.sensorFeasibilityTaskId + "\n"
                + "nbSegments : " + segments.size());

        //DUMMY : wait ten seconds per segment to simulate Submission of this segment
        try {
            for (int i = 0; i < segments.size(); i++) {

                Thread.sleep(1000);

                int percent = Algorithms.getRandom(0, 100);
                System.out.println("Percent : " + percent);
                Status segmentStatus = null;
                if (percent > 10) { //Segment is acquirable
                    segmentStatus = this.planningDBHandler.setSegmentPlannedStatus(
                            segments.get(i).getSegmentId(),
                            segments.get(i).getGroundStationDownlink().getEndOfVisibility().getTime());

                    accepted.add(segments.get(i));
                    //Set estimatedTOC to this segment's estimatedTOC
                    if (estimatedTOC.before(segments.get(i).getGroundStationDownlink().getEndOfVisibility())) {
                        estimatedTOC = segments.get(i).getGroundStationDownlink().getEndOfVisibility();
                    }
                } else {//Segment is not acquirable
                    segmentStatus = this.planningDBHandler.setSegmentRejectedStatus(segments.get(i).getSegmentId());
                }

                System.out.println("Segment " + segments.get(i).getSegmentId() + " : " + segmentStatus);
                segments.get(i).setStatus(segmentStatus);
            }

        } catch (InterruptedException ex) {
            System.out.println("Task has been cancelled.");
        }

        if (!isCancelled && accepted.isEmpty()) {
            status = this.planningDBHandler.addNewFailedStatus(sensorPlanningTaskId);
            return;
        } else {
            double p = accepted.size() * 100 / segments.size();
            System.out.println("p : " + p);
            percentCompletion = (int) p;
            if (isCancelled) {
                status = this.planningDBHandler.addNewCancelledStatus(sensorPlanningTaskId, percentCompletion);
                if (accepted != null && !accepted.isEmpty()) {
                    for (Segment segment : accepted) {
                        System.out.println("Segment : " + segment.getSegmentId() + " - " + segment.getStatus().getIdentifier());
                        Status segmentStatus = this.planningDBHandler.setSegmentCancelledStatus(segment.getSegmentId());
                        segment.setStatus(segmentStatus);
                    }
                }
            } else {
                status = this.planningDBHandler.addNewAcceptedStatus(sensorPlanningTaskId, percentCompletion, estimatedTOC.getTime());
            }
        }

        System.out.println("Accepted part finished. \nStatus is : " + status + ""
                + "\nNb segments left : " + accepted.size());

        /**
         * At this point, we only have segments that have been accepted and will
         * be acquired in the <accepted> list, the status of all segments are
         * still in the <segments> list, so by difference you can find the
         * rejected ones
         */
    }//End function

    /**
     * This function is to be called only after call to the submitPlanning
     * function Starting from the <accepted> field, it loops in that list and
     * waits for the segments in it to be completed (that is when "now" reaches
     * their time of completion) Then it will add these segments to the
     * <acquired> list, or set them to failed (one percent random chance)
     *
     * @param accepted
     * @throws SQLException
     */
    public void waitForAcquisitions() throws SQLException {

        try {
            while (!isComplete && !isCancelled) {

                Thread.sleep(3000);
                checkAcquisitions();
            }

        } catch (InterruptedException ex) {
            System.out.println("Task has been cancelled.");
        }

        System.out.println("Acquired part finished. \nStatus is : " + status + ""
                + "\nNb segments left : " + acquired.size());
    }

    public void checkAcquisitions() throws SQLException {

        if (acquired == null) {
            acquired = new ArrayList<>();
        }
        isComplete = false;
        nbComplete = 0;

        for (int i = 0; i < accepted.size(); i++) {

            //When the segment is finished it takes a new status (ACQUIRED or FAILED)
            //So this permits to avoid testing finished segments
            if (accepted.get(i).getStatus().getIdentifier().equalsIgnoreCase("PLANNED")) {
                //If the downlink should have been performed
                if (accepted.get(i).getGroundStationDownlink().getEndOfVisibility().before(
                        Calendar.getInstance())) {

                    //Random : put segment failed if percent <= 1
                    int percent = Algorithms.getRandom(0, 100);

                    Status segmentStatus = null;
                    if (percent > 1) { //Segment is acquirable
                        segmentStatus = this.planningDBHandler.setSegmentAcquiredStatus(
                                accepted.get(i).getSegmentId(),
                                accepted.get(i).getGroundStationDownlink().getEndOfVisibility().getTime());

                        //                        accepted.get(i).setStatus(segmentStatus);

                        acquired.add(accepted.get(i));
                        //Set estimatedTOC to this segment's estimatedTOC
                        if (estimatedTOC.before(accepted.get(i).getGroundStationDownlink().getEndOfVisibility())) {
                            estimatedTOC = accepted.get(i).getGroundStationDownlink().getEndOfVisibility();
                        }
                        
                    } else {//Segment is not acquirable
                        segmentStatus = this.planningDBHandler.setSegmentFailedStatus(accepted.get(i).getSegmentId());
                    }
                    accepted.get(i).setStatus(segmentStatus);

                    nbComplete++;
                }
            }
        }

        if (nbComplete == accepted.size()) {
            System.out.println("Complete : " + nbComplete + " / " + accepted.size());
            isComplete = true;
        }

        if (isComplete || isCancelled) {

            if (!isCancelled && acquired.isEmpty()) {
                status = this.planningDBHandler.addNewFailedStatus(sensorPlanningTaskId);
            } else {
                int p = acquired.size() * 100 / segments.size();
                System.out.println("p : " + p);
                percentCompletion = (int) p;

                if (isCancelled) {
                    status = this.planningDBHandler.addNewCancelledStatus(sensorPlanningTaskId, percentCompletion);
                    for (Segment segment : accepted) {
                        System.out.println("Segment : " + segment.getSegmentId() + " - " + segment.getStatus().getIdentifier());
                        if ("POTENTIAL PLANNED".contains(segment.getStatus().getIdentifier())) {
                            Status segmentStatus = this.planningDBHandler.setSegmentCancelledStatus(segment.getSegmentId());
                            segment.setStatus(segmentStatus);
                        }
                    }
                } else {
                    status = this.planningDBHandler.addNewFinishedStatus(sensorPlanningTaskId, percentCompletion, estimatedTOC.getTime());
                }
            }
        }
    }
}
