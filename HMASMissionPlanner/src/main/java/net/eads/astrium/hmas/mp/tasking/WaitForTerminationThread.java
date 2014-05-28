/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.mp.tasking;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author re-sulrich
 */
public class WaitForTerminationThread extends Thread {

    
    
    public WaitForTerminationThread() {
        super();
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WaitForTerminationThread.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Thread interrupted");
            }
            List<String> finishedTasks = new ArrayList<>();
            
            for (String task : SensorFeasibilityPlanningServiceHandler.currentTasks.keySet()) {
                try {
                    SensorFeasibilityPlanningServiceHandler t = SensorFeasibilityPlanningServiceHandler.currentTasks.get(task);
                    //If the planning part is finished
                    //Check if some segments are finished
                    if (!t.executionType.equals(SensorFeasibilityPlanningServiceHandler.ExecutionType.allSynchronous) && 
                            t.getStatus().getIdentifier().equals("PLANNING ACCEPTED")) {
                        t.checkAcquisitions();
                        if (t.isComplete || t.isCancelled) {
                            System.out.println("Removing from current tasks : " + t.getSensorPlanningTaskId() + " : " +
                                    t.isComplete + " - " + t.isCancelled);
                            finishedTasks.add(task);
                        }
                    }
                    
                } catch (SQLException ex) {
                    System.out.println("" + ex.getClass().getName() + " : " + ex.getMessage());
                }
            }
            //Removing the finished tasks from the Map
            for (String task : finishedTasks) {
                SensorFeasibilityPlanningServiceHandler.currentTasks.remove(task);
            }
        }
    }
}
