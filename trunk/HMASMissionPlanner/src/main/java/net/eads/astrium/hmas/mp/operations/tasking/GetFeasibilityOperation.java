/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetFeasibilityOperation.java
 *   File Type                                          :               Source Code
 *   Description                                        :                *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (coffee) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 */
package net.eads.astrium.hmas.mp.operations.tasking;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.eads.astrium.hmas.eocfihandler.hmaseocfihandler.EoCfiHndlrError;
import net.eads.astrium.hmas.util.DateHandler;
import net.eads.astrium.hmas.util.structures.SatellitePlatform;
import net.eads.astrium.hmas.util.structures.tasking.OPTTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.hmas.util.structures.tasking.SARTaskingParameters;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.eads.astrium.hmas.util.structures.tasking.Status;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import net.eads.astrium.hmas.exceptions.GetFeasibilityFault;
import net.eads.astrium.hmas.mp.feasibilityanalysis.OPTSensorFeasibilityAnalysisHandler;
import net.eads.astrium.hmas.mp.feasibilityanalysis.SARSensorFeasibilityAnalysisHandler;
import net.eads.astrium.hmas.mp.feasibilityanalysis.SensorFeasibilityAnalysisHandler;
import net.eads.astrium.hmas.util.Algorithms;
import net.eads.astrium.hmas.util.structures.TimePeriod;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Circle;
import net.eads.astrium.hmas.util.structures.tasking.geometries.Geometry;
import net.opengis.eosps.x20.AcquisitionAngleType;
import net.opengis.eosps.x20.AcquisitionParametersOPTType;
import net.opengis.eosps.x20.AcquisitionParametersSARType;

import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.FeasibilityStudyDocument;
import net.opengis.eosps.x20.FeasibilityStudyType;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseType;
import net.opengis.eosps.x20.GetFeasibilityType;
import net.opengis.eosps.x20.IncidenceRangeType;
import net.opengis.eosps.x20.MonoscopicAcquisitionType;
import net.opengis.eosps.x20.PointingRangeType;
import net.opengis.eosps.x20.SegmentPropertyType;
import net.opengis.eosps.x20.SegmentType;
import net.opengis.eosps.x20.SegmentValidationDataDocument;
import net.opengis.eosps.x20.StatusReportType;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.ValidationParametersOPTType;
import net.opengis.eosps.x20.ValidationParametersSARType;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.swe.x20.CategoryType;
import xint.esa.earth.eop.DownlinkInformationDocument;
import xint.esa.earth.eop.DownlinkInformationType;
import xint.esa.earth.eop.EarthObservationEquipmentType;
import xint.esa.earth.eop.InstrumentType;
import xint.esa.earth.eop.OrbitTypeValueType;
import xint.esa.earth.eop.PlatformType;
import xint.esa.earth.eop.SensorType;

/**
 * @file GetFeasibilityOperation.java
 * @author Benjamin VADANT <benjaminvadant@gmail.com>
 * @version 1.0
 *
 * @section LICENSE
 *
 * To be defined
 *
 * @section DESCRIPTION
 *
 * The GetFeasibility Operation realizes a feasibility analysis of a given
 * acquisition.
 *
 * This class is not currently working as some part of the request analysis are
 * not yet available.
 *
 * It is now always sending back the same analysis results defined in the
 * getFeasibility method.
 */
public class GetFeasibilityOperation extends EOSPSOperation<MissionPlannerDBHandler, GetFeasibilityDocument, GetFeasibilityResponseDocument, GetFeasibilityFault> {

    
    private SatellitePlatform platform;
    private String sensorId;
    private String sensorType;
    
    private SensorFeasibilityAnalysisHandler feasibilityHandler;
    
    /**
     *
     * @param request
     */
    public GetFeasibilityOperation(MissionPlannerDBHandler loader, GetFeasibilityDocument request) {

        super(loader, request);
    }

    @Override
    public void validRequest() throws GetFeasibilityFault {
        
        try {
        
            //Loading data from the database
            platform = (this.getConfigurationLoader().getSatelliteLoader()).getSatellite();
            
            //----------------------------------------------------------------------------
            // Decoding request
            //----------------------------------------------------------------------------
            GetFeasibilityType feasibility = this.getRequest().getGetFeasibility();
            
            String procedure = feasibility.getProcedure();
            if (procedure == null || procedure.equals("")) {
                throw new GetFeasibilityFault("Please choose sensor or sensor type to task through the 'procedure' parameter.");
            }
            TaskingParametersType taskingParameters = feasibility.getEoTaskingParameters();
            if (taskingParameters == null || taskingParameters.getCoverageProgrammingRequest() == null) {
                throw new GetFeasibilityFault("Please fill in a TaskingParameter:CoverageProgrammingRequest structure to task a (set of) sensor(s).");
            }
            CoverageProgrammingRequestType coverage = taskingParameters.getCoverageProgrammingRequest();

            //Stores the sensors that the request is tasking and the associated feasibilityAnalysisHandlers
            sensorId = null;
            
            sensorType = null;
            
            //----------------------------------------------------------------------------
            //Verifying sensor
            //----------------------------------------------------------------------------
            if (procedure.equalsIgnoreCase("opt") || procedure.equalsIgnoreCase("sar")) {
                sensorType = procedure.toUpperCase();
                List<String> sensors = (this.getConfigurationLoader().getSatelliteLoader()).getSensorsIds(sensorType);
                if (sensors != null) {
                    sensorId = sensors.get(0);
                }
                else {
                    throw new GetFeasibilityFault("No sensors were found for type " + sensorType);
                }
            }
            else
            {
                //Load this satellite's sensors
                List<String> sensors = (this.getConfigurationLoader().getSatelliteLoader()).getSensorsIds();
                boolean isSensorHandled = false;
                for (String sensor : sensors) {
                    if (procedure.equals(sensor)) {
                        isSensorHandled = true;
                        sensorId = procedure;
                    }
                }
                //If the asked sensor could not be found, throw exception
                if (!isSensorHandled) {
                    throw new GetFeasibilityFault("Sensor " + procedure + "is not handled by this server.");
                }
                sensorType = (this.getConfigurationLoader().getSatelliteLoader()).getSensorType(procedure);
            }
            
            System.out.println("Sensor is : " + sensorId + " ( " + sensorType + " ).");
            
            //----------------------------------------------------------------------------
            //Saving request in database
            //----------------------------------------------------------------------------
            double minIncAz = -90;
            double maxIncAz = 90;
            double minIncEl = -90;
            double maxIncEl = 90;
            double minPoiAc = -90;
            double maxPoiAc = 90;
            double minPoiAl = -90;
            double maxPoiAl = 90;

            Geometry coords = null;
            String roiType = null;
            
            System.out.println("" + coverage.getRegionOfInterest().xmlText());
            
            if (coverage.getRegionOfInterest() != null &&
                    coverage.getRegionOfInterest().getPolygonArray() != null &&
                    coverage.getRegionOfInterest().getPolygonArray().length > 0 && 
                    coverage.getRegionOfInterest().getPolygonArray(0) != null &&
                    coverage.getRegionOfInterest().getPolygonArray(0).getExterior() != null) {

                CoordinatesType c = ((LinearRingType)
                        coverage.getRegionOfInterest().getPolygonArray(0).getExterior().getAbstractRing()).getCoordinates();

                String dec = c.getDecimal();
                String cs = c.getCs();
                String ts = c.getTs();

                String[] p = c.getStringValue().split(ts);

                List<Point> points = null;

                if (p != null && p.length > 0 && !p[0].equals("")) {

                    points = new ArrayList<>();
                    for (int i = 0; i < p.length; i++) {

                        String[] coord = p[i].split(cs);

                        double latitude = Double.valueOf(coord[0].replace(dec, "."));
                        double longitude = Double.valueOf(coord[1].replace(dec, "."));

                        points.add(new Point(longitude, latitude, 0.0));
                    }
                }
                Polygon pol = new Polygon(points);
                
                System.out.println("");
                System.out.println("Polygon before clockwise : " + pol.printCoordinatesGML());
                System.out.println("");
                
                Algorithms.getClockWise(pol);
                
                System.out.println("");
                System.out.println("Polygon after clockwise : " + pol.printCoordinatesGML());
                System.out.println("");
                
                coords = pol;
                roiType = "POLYGON";
            }

            if (coverage.getRegionOfInterest() != null &&
                    coverage.getRegionOfInterest().getCircleArray()!= null &&
                    coverage.getRegionOfInterest().getCircleArray().length > 0 && 
                    coverage.getRegionOfInterest().getCircleArray(0) != null) {

                CoordinatesType c = 
                        coverage.getRegionOfInterest().getCircleArray(0).getCoordinates();
                
                
                String dec = c.getDecimal();
                String cs = c.getCs();
                String ts = c.getTs();

                String[] p = c.getStringValue().split(ts);

                List<Point> points = null;

                if (p != null && 
                        p.length > 2 && 
                        !p[0].equals("")) {

                    points = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        String[] coord = p[i].split(cs);

                        double latitude = Double.valueOf(coord[0].replace(dec, "."));
                        double longitude = Double.valueOf(coord[1].replace(dec, "."));

                        points.add(new Point(longitude, latitude, 0.0));
                    }
                    coords = new Circle(points.get(0), points.get(1), points.get(2));
                    roiType = "CIRCLE";
                }
            }
            
            if (coords != null)
                System.out.println("Geometry : " + coords.getClass().getName() + " : " + coords.printCoordinatesGML());
            else
                System.out.println("Geometry null.");
            
            //-----------------------------------------------------------------------------------------------------------
            // Parsing dates :
            // - parse begin and end dates from request
            // - get sensor availibilities for that given period
            // - TOI is SensorAvailibilities
            //-----------------------------------------------------------------------------------------------------------
            Date begin = null;
            Date end = null;

            if (coverage.getTimeOfInterest() != null &&
                    coverage.getTimeOfInterest().getSurveyPeriodArray() != null &&
                    coverage.getTimeOfInterest().getSurveyPeriodArray(0).getTimePeriod() != null)
            {
                TimePeriodType toi = coverage.getTimeOfInterest().getSurveyPeriodArray(0).getTimePeriod();

                String b = toi.getBeginPosition().getStringValue();
                String e = toi.getEndPosition().getStringValue();

                System.out.println("EOSPS : Parsing dates : inputs : " + b + " - " + e);

                if (b != null) {
                    try {
                        begin = DateHandler.parseDate(b);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        throw new GetFeasibilityFault("Error parsing date : " + b);
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
                if (e != null) {
                    try {
                        end = DateHandler.parseDate(e);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        throw new GetFeasibilityFault("Error parsing date : " + e);
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
            List<TimePeriod> times = Arrays.asList(new TimePeriod(begin, end));

            //-----------------------------------------------------------------------------------------------------------
            // Parsing other tasking parameters
            //-----------------------------------------------------------------------------------------------------------
            
            if (coverage.getAcquisitionType() == null) {
                coverage.addNewAcquisitionType();
            }
            if (coverage.getAcquisitionType().getMonoscopicAcquisitionArray() == null ||
                    coverage.getAcquisitionType().getMonoscopicAcquisitionArray().length == 0) {
                coverage.getAcquisitionType().addNewMonoscopicAcquisition().addNewBHRatio();
            }
            
            MonoscopicAcquisitionType monoAcq = coverage.getAcquisitionType().getMonoscopicAcquisitionArray(0);

            
            if (monoAcq != null) {
                System.out.println("" + monoAcq.xmlText());

                AcquisitionAngleType acqAngle = monoAcq.getAcquisitionAngle();
                if (acqAngle != null) {
                    IncidenceRangeType inc = acqAngle.getIncidenceRange();
                    PointingRangeType poi = acqAngle.getPointingRange();
                    if (inc != null) {
                        if (inc.getAzimuthAngle() != null)
                        {
                            try {
                                minIncAz = Double.valueOf(inc.getAzimuthAngle().getValue().get(0).toString());
                                maxIncAz = Double.valueOf(inc.getAzimuthAngle().getValue().get(1).toString());
                            } catch (NumberFormatException | NullPointerException e) { }
                        }
                        if (inc.getElevationAngle() != null)
                        {
                            try {
                                minIncEl = Double.valueOf(inc.getElevationAngle().getValue().get(0).toString());
                                maxIncEl = Double.valueOf(inc.getElevationAngle().getValue().get(1).toString());
                            } catch (NumberFormatException | NullPointerException e) { }
                        }
                    }
                    if (poi != null) {

                        if (poi.getAcrossTrackAngle() != null)
                        {
                            try {
                                minPoiAc = Double.valueOf(poi.getAcrossTrackAngle().getValue().get(0).toString());
                                maxPoiAc = Double.valueOf(poi.getAcrossTrackAngle().getValue().get(1).toString());
                            } catch (NumberFormatException | NullPointerException e) { }
                        }

                        if (poi.getAlongTrackAngle() != null)
                        {
                            try {
                                minPoiAl = Double.valueOf(poi.getAlongTrackAngle().getValue().get(0).toString());
                                maxPoiAl = Double.valueOf(poi.getAlongTrackAngle().getValue().get(1).toString());
                            } catch (NumberFormatException | NullPointerException e) { }
                        }
                    }
                }

                //-----------------------------------------------------------------------------------------------------------
                    //SensorType specific parameters
                //-----------------------------------------------------------------------------------------------------------
                
                if (sensorType.equalsIgnoreCase("SAR")) {

                    AcquisitionParametersSARType sarAcqParams = null;
                    if (monoAcq.getAcquisitionParameters() != null)
                        sarAcqParams = monoAcq.getAcquisitionParameters().getAcquisitionParametersSAR();

                    ValidationParametersSARType sarValParam = null;
                    if (coverage.getValidationParameters() != null)
                        sarValParam = coverage.getValidationParameters().getValidationParametersSAR();

                    int minGR = 0;
                    int maxGR = 100;
                    List<String> instModes = null;
                    boolean fusionAccepted = true;
                    List<String> polModes = null;

                    double maxNoiseLevel = 0.0;
                    double maxAmbiguityLevel = 0.0;

                    if (sarAcqParams != null) {

                        if (sarAcqParams.getGroundResolution() != null) {

                            List gr = sarAcqParams.getGroundResolution().getValue();
                            if (gr.get(0) != null)
                                try {
                                    minGR = Integer.valueOf(gr.get(0).toString());
                                } catch (NumberFormatException e) {}
                            if (gr.size() > 1 && gr.get(1) != null)
                                try {
                                    maxGR = Integer.valueOf(gr.get(1).toString());
                                } catch (NumberFormatException e) {}
                        }

                        if (
                                sarAcqParams.getInstrumentMode() != null && 
                                sarAcqParams.getInstrumentMode().getValue() != null && 
                                !sarAcqParams.getInstrumentMode().getValue().equals("")) {

                            instModes = Arrays.asList(sarAcqParams.getInstrumentMode().getValue().split(","));
                        }
                        else {
                            instModes = new ArrayList<>();
                            Set<String> iM = (this.getConfigurationLoader().getSatelliteLoader()).getInstrumentModes(sensorId).keySet();
                            instModes.addAll(iM);
                        }

                        System.out.println("Found " + instModes.size() + " instrument modes.");

                        if (sarAcqParams.getFusionAccepted() != null)
                            fusionAccepted = sarAcqParams.getFusionAccepted().getValue();

                        if (sarAcqParams.getPolarizationMode() != null && 
                                sarAcqParams.getPolarizationMode().getValue() != null && 
                                !sarAcqParams.getPolarizationMode().getValue().equals("")) {

                            polModes = new ArrayList<>();
                            polModes.addAll(Arrays.asList(sarAcqParams.getPolarizationMode().getValue().split(",")));
                        } else {
                            
                            polModes = new ArrayList<>();
                            for (String polMode : polModes) {
                                Set<String> pM = (this.getConfigurationLoader().getSatelliteLoader()).getPolarisationModes(sensorId, polMode).keySet();
                                polModes.addAll(pM);
                            }
                        }
                    }
                    
                    if (instModes == null || instModes.isEmpty()) {

                        instModes = new ArrayList<>();
                        Set<String> iM = (this.getConfigurationLoader().getSatelliteLoader()).getInstrumentModes(sensorId).keySet();
                        instModes.addAll(iM);
                    }
                    
                    if (sarValParam != null) {

                        if (sarValParam.getMaxNoiseLevel() != null)
                            maxNoiseLevel = sarValParam.getMaxNoiseLevel().getValue();

                        if (sarValParam.getMaxAmbiguityLevel() != null)
                            maxAmbiguityLevel = sarValParam.getMaxAmbiguityLevel().getValue();
                    }

                    SARTaskingParameters sarParameters = new SARTaskingParameters(
                            maxNoiseLevel, 
                            maxAmbiguityLevel, 
                            fusionAccepted, 
                            polModes, 
                            minIncAz, maxIncAz, minIncEl, maxIncEl, minPoiAc, maxPoiAc, minPoiAl, maxPoiAl, 
                            coords, roiType, 
                            times, 
                            minGR, maxGR, 
                            instModes);

                    this.feasibilityHandler = new SARSensorFeasibilityAnalysisHandler(
                            sarParameters, 
                            (this.getConfigurationLoader()).getSatelliteId(),
                            sensorId, 
                            this.getConfigurationLoader());
                }

                if (sensorType.equalsIgnoreCase("OPT")) {

                    int minGR = 0;
                    int maxGR = 100;
                    List<String> instModes = null;
                    boolean fusionAccepted = true;
                    double minLuminosity = 100.0;

                    double maxCloudCover = 100.0;
                    double maxSnowCover = 100.0;
                    double maxSunGlint = 100.0;
                    boolean hazeAccepted = true;
                    boolean sandWindAccepted = true;

                    String meteoServerId = null;

                    AcquisitionParametersOPTType optAcqParams = null;
                    if (monoAcq.getAcquisitionParameters() != null)
                        optAcqParams = monoAcq.getAcquisitionParameters().getAcquisitionParametersOPT();


                    ValidationParametersOPTType optValParam = null;
                    if (coverage.getValidationParameters() != null)
                        optValParam = coverage.getValidationParameters().getValidationParametersOPT();

                    if (optAcqParams != null)
                    {
                        if (optAcqParams.getGroundResolution() != null) {

                            List gr = optAcqParams.getGroundResolution().getValue();
                            if (gr.get(0) != null)
                                try {
                                    minGR = Integer.valueOf(gr.get(0).toString());
                                } catch (NumberFormatException e) {}
                            if (gr.size() > 1 && gr.get(1) != null)
                                try {
                                    maxGR = Integer.valueOf(gr.get(1).toString());
                                } catch (NumberFormatException e) {}
                        }

                        if (
                                optAcqParams.getInstrumentMode() != null && 
                                optAcqParams.getInstrumentMode().getValue() != null && 
                                !optAcqParams.getInstrumentMode().getValue().equals("")) {
                            instModes = Arrays.asList(optAcqParams.getInstrumentMode().getValue().split(","));
                        }
                        else {
                            instModes = new ArrayList<>();
                            Set<String> iM = (this.getConfigurationLoader().getSatelliteLoader()).getInstrumentModes(sensorId).keySet();
                            instModes.addAll(iM);
                        }
                        
                        System.out.println("Found " + instModes.size() + " instrument modes.");

                        if (optAcqParams.getFusionAccepted() != null)
                            fusionAccepted = optAcqParams.getFusionAccepted().getValue();
                        if (optAcqParams.getMinLuminosity() != null)
                            minLuminosity = optAcqParams.getMinLuminosity().getValue();
                    }
                    
                    if (instModes == null || instModes.isEmpty()) {

                        instModes = new ArrayList<>();
                        Set<String> iM = (this.getConfigurationLoader().getSatelliteLoader()).getInstrumentModes(sensorId).keySet();
                        instModes.addAll(iM);
                    }
                    
                    if (optValParam != null) {
                        if (optValParam.getMaxCloudCover() != null)
                            maxCloudCover = optValParam.getMaxCloudCover().getValue();
                        if (optValParam.getMaxSnowCover() != null)
                            maxSnowCover = optValParam.getMaxSnowCover().getValue();
                        if (optValParam.getMaxSunGlint() != null)
                            maxSunGlint = optValParam.getMaxSunGlint().getValue();
                        if (optValParam.getHazeAccepted() != null)
                            hazeAccepted = optValParam.getHazeAccepted().getValue();
                        if (optValParam.getSandWindAccepted() != null)
                            sandWindAccepted = optValParam.getSandWindAccepted().getValue();
                        
                    }

                    OPTTaskingParameters optParameters = new OPTTaskingParameters(
                            maxCloudCover, maxSnowCover,maxSunGlint, hazeAccepted, sandWindAccepted, 
                            minLuminosity, fusionAccepted, 
                            minIncAz, maxIncAz, minIncEl, maxIncEl, minPoiAc, maxPoiAc, minPoiAl, maxPoiAl, 
                            coords, roiType, 
                            times, 
                            minGR, maxGR, 
                            instModes);
                    
                    if (meteoServerId != null) {
                        optParameters.setMeteoServerId(meteoServerId);
                    }

                    this.feasibilityHandler = new OPTSensorFeasibilityAnalysisHandler(
                            optParameters, 
                            (this.getConfigurationLoader()).getSatelliteId(),
                            sensorId, 
                            this.getConfigurationLoader());
                }
            }
        } catch (SQLException ex) {
                        ex.printStackTrace();
            
            throw new GetFeasibilityFault("SQLException while parsing the request : " + ex.getMessage());
        } catch (ParseException ex) {
                        ex.printStackTrace();
            throw new GetFeasibilityFault("ParseException while parsing the request : " + ex.getMessage());
        } catch (EoCfiHndlrError ex) {
                        ex.printStackTrace();
            throw new GetFeasibilityFault("EoCfiHndlrError while initializing EOCFI handler : " + ex.getMessage());
        }
    }

    @Override
    public void executeRequest() throws GetFeasibilityFault {

        //Handling request
        this.validRequest();
        
        try {
            this.feasibilityHandler.doFeasibility();
        
        } catch (Exception e) {
                        e.printStackTrace();
            throw new GetFeasibilityFault(e.getClass().getName() + " : " + e.getMessage());
        }
        GetFeasibilityResponseDocument response = this.createResponse();
        //Generating the response object and saving it in response parameter (in EOSPSOperation)
        this.setResponse(response);
//        this.setResponse(response);
    }
    
    /**
     * Generates the XML response from the data contained in the feasibilityHandler
     * NB : this function is only to be used after the feasibilityHandler has performed the doFeasibility method
     * @return the XML response to send back to the client
     */
    private GetFeasibilityResponseDocument createResponse() {
        
        Status status = this.feasibilityHandler.getStatus();
        
        GetFeasibilityResponseDocument response = GetFeasibilityResponseDocument.Factory.newInstance();
        GetFeasibilityResponseType resp = response.addNewGetFeasibilityResponse();
        
        //------------------------------------------------------------
        //Status report
        //------------------------------------------------------------
        StatusReportType statusReport = resp.addNewResult().addNewStatusReport();
        
        statusReport.setProcedure(this.sensorId);
        
        statusReport.setTask(this.feasibilityHandler.getTaskId());
        
        statusReport.setIdentifier(status.getIdentifier());
        statusReport.setUpdateTime(DateHandler.getCalendar(status.getUpdateTime()));
        statusReport.setEstimatedToC(DateHandler.getCalendar(status.getEstimatedTimeOfCompletion()));
        statusReport.setPercentCompletion(status.getPercentCompletion());
        statusReport.addNewStatusMessage().setStringValue(status.getMessage());

        //Add the parameters of the request to the response (informative)
        statusReport.addNewEoTaskingParameters().set(
                this.getRequest().getGetFeasibility().getEoTaskingParameters()
            );
        //------------------------------------------------------------
        //Feasibility study
        //------------------------------------------------------------
        
        FeasibilityStudyDocument study = this.getFeasibilityStudy();
        
        resp.addNewExtension().set(study);
        
        return response;
    }
    
    
    /**
     * Creates the FeasibilityStudyDocument from the data of the SensorFeasibilityAnalysisHandler
     * @return 
     */
    private FeasibilityStudyDocument getFeasibilityStudy() {
        
        FeasibilityStudyDocument studyDoc = FeasibilityStudyDocument.Factory.newInstance();
        FeasibilityStudyType study = studyDoc.addNewFeasibilityStudy();
        
        study.addNewEstimatedCost().setDoubleValue(this.feasibilityHandler.getEstimatedCost());
        study.getEstimatedCost().setUom(this.feasibilityHandler.getCurrency());
        study.setId(this.feasibilityHandler.getTaskId());
        
        study.setSegmentArray(this.addSegments());
        
        return studyDoc;
    }
    
    /**
     * Creates the Segments response structure from the data of the SensorFeasibilityAnalysisHandler
     * @return 
     */
    private SegmentPropertyType[] addSegments() {
        
        List<Segment> segments = this.feasibilityHandler.getSegments();
        
        SegmentPropertyType[] ss = new SegmentPropertyType[segments.size()];
        
        int i = 0;
        for (Segment segment : segments) {
            
            SegmentPropertyType s = SegmentPropertyType.Factory.newInstance();
            
            SegmentType seg = s.addNewSegment();
            
            //Times
            seg.setAcquisitionStartTime(segment.getStartOfAcquisition());
            seg.setAcquisitionStopTime(segment.getEndOfAcquisition());

            seg.setId(segment.getSegmentId());

            //EOP
            EarthObservationEquipmentType eoEquipment = seg.addNewAcquisitionMethod().addNewEarthObservationEquipment();
            //Sensor
            SensorType sensor = eoEquipment.addNewSensor().addNewSensor();
            //Sensor Type
            CodeType instType = sensor.addNewSensorType();
            instType.setCodeSpace("urn:ogc:def:property:OGC:sensorType");
            instType.setStringValue(sensorType);
            //Instrument Mode
            CategoryType instMode = CategoryType.Factory.newInstance();
            instMode.setDefinition("urn:ogc:def:property:CEOS:eop:InstrumentMode");
            instMode.setValue(segment.getInstrumentMode());
            sensor.addNewOperationalMode().set(instMode);
            //Instrument identifier
            InstrumentType instrument = eoEquipment.addNewInstrument().addNewInstrument();
            instrument.setShortName(sensorId);
            
            //Platform
            PlatformType plat = eoEquipment.addNewPlatform().addNewPlatform();
            plat.setSerialIdentifier(platform.getId());
            
            if ("LEO GEO".contains(platform.getOrbit().getOrbitType())) {
                plat.setOrbitType(OrbitTypeValueType.Enum.forString(platform.getOrbit().getOrbitType()));
            }
            else
            {
                plat.setOrbitType(OrbitTypeValueType.Enum.forString("LEO"));
            }
            
            plat.setShortName(platform.getName());

            eoEquipment.addNewIdentifier().setStringValue(platform.getId() + " - " + sensorId);

            eoEquipment.addNewDescription().setStringValue(platform.getDescription());

            //Polygon
            PolygonType polygon = seg.addNewFootprint().addNewPolygon();

            CoordinatesType coords = CoordinatesType.Factory.newInstance();
            coords.setDecimal(".");
            coords.setCs(",");
            coords.setTs(" ");

            coords.setStringValue(segment.getPolygon().printCoordinatesGML());

            LinearRingType lineRing = LinearRingType.Factory.newInstance();
            lineRing.setCoordinates(coords);
            polygon.addNewExterior().setAbstractRing(lineRing);
            
            SegmentValidationDataDocument valData = SegmentValidationDataDocument.Factory.newInstance();
            valData.addNewSegmentValidationData().setCloudCoverSuccessRate(segment.getCloudCoverSuccessRate());
            seg.addNewExtension().set(valData);
            
            DownlinkInformationDocument downlinkDoc = DownlinkInformationDocument.Factory.newInstance();
            DownlinkInformationType downlink = downlinkDoc.addNewDownlinkInformation();
            
            downlink.setAcquisitionDate(segment.getGroundStationDownlink().getStartOfVisibility());
            
            CategoryType acqStation = CategoryType.Factory.newInstance();
            acqStation.setDefinition("urn:ogc:def:property:CEOS:eop:GroundStation");
            acqStation.setValue(segment.getGroundStationDownlink().getGroundStationId());
            
            downlink.addNewAcquisitionStation().set(acqStation);
            
            seg.addNewExtension().set(downlinkDoc);
            
            ss[i] = s;
            i++;
        }
        
        return ss;
    }
}
