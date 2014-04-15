/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetCapabilitiesOperation.java
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
 */package net.eads.astrium.hmas.operations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.eads.astrium.hmas.database.EOSPSDBHandler;
import net.eads.astrium.hmas.dbhandler.SensorLoader;
import net.eads.astrium.hmas.util.structures.Sensor;
import net.eads.astrium.hmas.exceptions.GetCapabilitiesFault;
import net.eads.astrium.hmas.util.Constants;
import net.eads.astrium.hmas.workers.EOSPSBasicWorker;
import net.eads.astrium.hmas.workers.EOSPSCancellationWorker;
import net.eads.astrium.hmas.workers.EOSPSFeasibilityPlanningWorker;
import net.eads.astrium.hmas.workers.EOSPSFeasibilityWorker;
import net.eads.astrium.hmas.workers.EOSPSPlanningWorker;
import net.eads.astrium.hmas.workers.EOSPSReservationWorker;

import org.apache.xmlbeans.GDuration;
import org.oasisOpen.docs.wsn.t1.TopicSetType;

import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.ows.x11.DomainType;
import net.opengis.ows.x11.GetCapabilitiesType;
import net.opengis.ows.x11.OperationDocument;
import net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata;
import net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification;
import net.opengis.sps.x21.CapabilitiesDocument;
import net.opengis.sps.x21.CapabilitiesType;
import net.opengis.sps.x21.CapabilitiesType.Contents;
import net.opengis.sps.x21.CapabilitiesType.Notifications;
import net.opengis.sps.x21.GetCapabilitiesDocument;
import net.opengis.sps.x21.SPSContentsType;
import net.opengis.sps.x21.SensorOfferingType;
import net.opengis.sps.x21.SensorOfferingType.ObservableArea;
import net.opengis.sps.x21.SensorOfferingType.ObservableArea.ByPolygon;
import net.opengis.sps.x21.TaskDocument;
import net.opengis.sps.x21.TaskType;
import net.opengis.swes.x21.AbstractContentsType.Offering;
import net.opengis.swes.x21.FilterDialectMetadataType;
import net.opengis.swes.x21.NotificationProducerMetadataType;
import net.opengis.swes.x21.NotificationProducerMetadataType.ServedTopics;
import net.opengis.swes.x21.NotificationProducerMetadataType.SupportedDialects;

 
public class GetCapabilitiesOperation extends EOSPSOperation<EOSPSDBHandler, GetCapabilitiesDocument,CapabilitiesDocument, GetCapabilitiesFault> {
    private final EOSPSBasicWorker worker;
    
    private final String serverBaseURI;

    /**
     * 
     * @param request
     */
    public GetCapabilitiesOperation(
            EOSPSDBHandler loader, GetCapabilitiesDocument request, String serverBaseURI,
            EOSPSBasicWorker worker
            ) 
    {
        super(loader,request);
        this.serverBaseURI = serverBaseURI;
        this.worker = worker;
    }

    @Override
    public void validRequest() throws GetCapabilitiesFault {

    }

    @Override
    public void executeRequest() throws GetCapabilitiesFault {

            this.validRequest();

            GetCapabilitiesDocument req = this.getRequest();

            GetCapabilitiesType caps = req.getGetCapabilities();

//		String[] sections = caps.getSections().getSectionArray();
//		
//		if (sections != null)
//		{
//			if (sections.length > 0)
//			{
//				for (int i = 0; i < sections.length; i++)
//				{
//					System.out.println(sections[i].toString());
//				}
//			}
//			else
//			{
//				System.out.println("sections empty");
//			}
//		}
//		else
//		{
//			System.out.println("sections null");
//		}
            System.out.println("Read request...");

            CapabilitiesDocument capabilitiesDocument = CapabilitiesDocument.Factory.newInstance();

            CapabilitiesType capabilities = capabilitiesDocument.addNewCapabilities();
            
            System.out.println("Service :");

            ServiceIdentification serviceId = capabilities.addNewServiceIdentification();

            serviceId.addNewTitle().setStringValue("MMFAS Sensor Planning Service");
            serviceId.addNewAbstract().setStringValue("MMFAS sps developed by Astrium LTD");
            serviceId.addNewServiceType().setStringValue("eosps");
            serviceId.addNewServiceTypeVersion().setStringValue("2.0.0");
            serviceId.addNewAccessConstraints().setStringValue("none");
            serviceId.addNewProfile().setStringValue("http://www.opengis.net/eosps/2.0");
            System.out.println("Service done...");
            
            System.out.println("Operation metadata :");
            OperationsMetadata op = this.getOperationsMetadata();
            capabilities.setOperationsMetadata(op);
            System.out.println("Operation metadata done...");

            System.out.println("Notifications :");
            Notifications nott = this.getNotifications();
            capabilities.setNotifications(nott);
            System.out.println("Notifications done...");

            System.out.println("Contents :");
            Contents cont = this.getContents();
            capabilities.setContents(cont);
            System.out.println("Contents done...");

            System.out.println("Writing Response");

            this.setResponse(capabilitiesDocument);
    }


    public Contents getContents() throws GetCapabilitiesFault
    {
        
        
        
        Contents contents = Contents.Factory.newInstance();
        SPSContentsType spsContents = contents.addNewSPSContents();
        
        spsContents.addNewProcedureDescriptionFormat().setStringValue("http://www.opengis.net/sensorML/1.0");
        spsContents.addNewObservableProperty().setStringValue("http://sweet.jpl.nasa.gov/2.0/physRadiation.owl#Radiance");
        spsContents.setMinStatusTime(new GDuration("PT48H"));

        spsContents.addNewSupportedEncoding().setStringValue("http://www.opengis.net/swe/2.0/TextEncoding");
        spsContents.addNewSupportedEncoding().setStringValue("http://www.opengis.net/swe/2.0/XMLEncoding");

        //Loading sensors from the database
        List<Sensor> sensors = null;
        try {
            SensorLoader mmfasHandler = this.getConfigurationLoader().getSensorLoader();
            System.out.println("sensor loader : " + mmfasHandler);
            sensors = mmfasHandler.loadSensors();//informationLoader.loadSensorsByPlatform(this.getServiceConfiguration().getPlatformId());
        } catch (SQLException ex) {
            ex.printStackTrace();
            
            throw new GetCapabilitiesFault("SQLException ( " + ex.getErrorCode() + " ) : " + ex.getMessage());
        } 
        
        if (sensors != null)
        {
            Offering[] offerings = new Offering[sensors.size()];
            int i = 0;
            for (Sensor sensor : sensors) {
                offerings[i]= getOffering(sensor);
                i++;
            }
            spsContents.setOfferingArray(offerings);
        }
        
        return contents;
    }

    public Offering getOffering(Sensor sensor)
    {
        Offering offering = Offering.Factory.newInstance();

        SensorOfferingType sensorOffering = SensorOfferingType.Factory.newInstance();

        sensorOffering.setDescription("Programming service for "+sensor.getSensorName()+" only");
        sensorOffering.setIdentifier(sensor.getSensorName());
//        sensorOffering.addNewExtension();
        sensorOffering.setProcedure(""+sensor.getSensorId());
        
        ObservableArea observableArea = sensorOffering.addNewObservableArea();

        ByPolygon byPolygon = observableArea.addNewByPolygon();
        PolygonType polygon = byPolygon.addNewPolygon();
        polygon.setId("gid0");

        AbstractRingPropertyType exterior = polygon.addNewExterior();

        LinearRingType linearRing = LinearRingType.Factory.newInstance();
        DirectPositionListType posList = linearRing.addNewPosList();
        posList.setSrsName("urn:ogc:def:crs:EPSG:6.17:4326");
        
        
        String coordinates = "" 
                + sensor.getMinLongitude() + " "
                + sensor.getMinLatitude()+ " "
                + sensor.getMaxLongitude()+ " "
                + sensor.getMinLatitude()+ " "
                + sensor.getMaxLongitude()+ " "
                + sensor.getMaxLatitude()+ " "
                + sensor.getMinLongitude() + " "
                + sensor.getMaxLatitude()+ "";
        posList.setStringValue(coordinates);																					//config files
        
        exterior.setAbstractRing(linearRing);

        offering.setAbstractOffering(sensorOffering);

        return offering;
    }
    
    
    
    public Notifications getNotifications() throws GetCapabilitiesFault
    {
        Notifications notifications = Notifications.Factory.newInstance();
        NotificationProducerMetadataType notificationProdMD = notifications.addNewNotificationProducerMetadata();

//        try {	
//            serverBaseURI = this.getServiceConfiguration().getServerBaseURI();
//        } catch (IOException ex) {
//            Logger.getLogger(GetCapabilitiesOperation.class.getName()).log(Level.SEVERE, null, ex);
//            serverBaseURI = "http://127.0.0.1:8080/DreamFASServices-1.0-SNAPSHOT/dream/fas/";
//        } catch (ConfFileNotFoundException ex) {
//            Logger.getLogger(GetCapabilitiesOperation.class.getName()).log(Level.SEVERE, null, ex);
//            serverBaseURI = "http://127.0.0.1:8080/DreamFASServices-1.0-SNAPSHOT/dream/fas/";
//        }
//
//        ProducerEndpoint producerEndpoint = notificationProdMD.addNewProducerEndpoint();
//        producerEndpoint
//                .addNewEndpointReference()
//                .addNewAddress()
//                .setStringValue(serverBaseURI);																			//config files

        //Supported dialects
        SupportedDialects supportedDialects = notificationProdMD.addNewSupportedDialects();
        FilterDialectMetadataType filterDialect = supportedDialects.addNewFilterDialectMetadata();
        filterDialect.addNewTopicExpressionDialect().setStringValue("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
        filterDialect.addNewTopicExpressionDialect().setStringValue("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete");
        filterDialect.addMessageContentDialect("http://www.w3.org/TR/1999/REC-xpath-19991116");
        //End Supported Dialects

        notificationProdMD.setFixedTopicSet(true);

        //Served Topics
        ServedTopics servedTopics = notificationProdMD.addNewServedTopics();

        TopicSetType topicSet = servedTopics.addNewTopicSet();

        TaskDocument taskEvent = TaskDocument.Factory.newInstance();

        TaskType task = taskEvent.addNewTask();


        // We don't have the right structure in topicSet

//		<swes:servedTopics>
//			<wstop:TopicSet>
//				<sps:TaskEvent>
//					<sps:TaskFailure wstop:topic="true"/>
//					<sps:TaskCancellation wstop:topic="true"/>
//					<sps:TaskCompletion wstop:topic="true"/>
//					<sps:TaskUpdate wstop:topic="true"/>
//					<sps:DataPublication wstop:topic="true"/>
//					<sps:TaskSubmission wstop:topic="true"/>
//				</sps:TaskEvent>
//				<sps:TaskingRequestEvent>
//					<sps:TaskingRequestAcceptance wstop:topic="true"/>
//					<sps:TaskingRequestRejection wstop:topic="true"/>
//				</sps:TaskingRequestEvent>
//				<eo:EOEvent>
//					<eo:SegmentPlanned wstop:topic="true"/>
//					<eo:SegmentAcquired wstop:topic="true"/>
//					<eo:SegmentValidated wstop:topic="true"/>
//					<eo:SegmentCancelled wstop:topic="true"/>
//					<eo:SegmentFailed wstop:topic="true"/>
//				</eo:EOEvent>
//			</wstop:TopicSet>
//		</swes:servedTopics>

        return notifications;
    }

    public OperationsMetadata getOperationsMetadata() throws GetCapabilitiesFault
    {
        List<String> sensors = new ArrayList<>();
        sensors.add("OPT");
        sensors.add("SAR");
        
        String serviceBaseAddr = "/"+Constants.WAR_FILE_PATH+"/" +
                this.getConfigurationLoader().getService()+"/" + 
                this.getConfigurationLoader().getServiceId() + "/";
        
        try {
            sensors = this.getConfigurationLoader().getSensorLoader().getSensorsIds();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new GetCapabilitiesFault("Database error.");
        }
        
        List<String> stations = null;
        try {
            stations = this.getConfigurationLoader().getSensorLoader().getStationsIds();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new GetCapabilitiesFault("Database error.");
        }
        
        OperationsMetadata operationMetadata = OperationsMetadata.Factory.newInstance();
        OperationDocument.Operation getcapa = operationMetadata.addNewOperation();
        getcapa.setName("GetCapabilities");
        getcapa.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                            +"eosps?service=SPS&request=GetCapabilities");
        OperationDocument.Operation desctask = operationMetadata.addNewOperation();
        desctask.setName("DescribeTasking");
        desctask.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                            +"eosps?service=SPS&request=DescribeTasking");
        DomainType desctaskprocedure = desctask.addNewConstraint();
        desctaskprocedure.setName("procedure");
        for (String sensor : sensors) {
            
            desctaskprocedure.addNewAllowedValues().addNewValue().setStringValue(sensor);
        }
        
        OperationDocument.Operation descsens = operationMetadata.addNewOperation();
        descsens.setName("DescribeSensor");
        descsens.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                            +"eosps?service=SPS&request=DescribeSensor");
        DomainType descsensprocedure = descsens.addNewConstraint();
        descsensprocedure.setName("procedure");
        for (String sensor : sensors) {
            
            descsensprocedure.addNewAllowedValues().addNewValue().setStringValue(sensor);
        }
        OperationDocument.Operation getsensav = operationMetadata.addNewOperation();
        getsensav.setName("GetSensorAvailibility");
        getsensav.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                            +"eosps?service=SPS&request=GetSensorAvailibility");
        DomainType getsensavprocedure = getsensav.addNewConstraint();
        getsensavprocedure.setName("procedure");
        for (String sensor : sensors) {
            
            getsensavprocedure.addNewAllowedValues().addNewValue().setStringValue(sensor);
        }
        OperationDocument.Operation getstatav = operationMetadata.addNewOperation();
        getstatav.setName("GetStationAvailibility");
        getstatav.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                            +"eosps?service=SPS&request=GetStationAvailibility");
        DomainType stat = getstatav.addNewConstraint();
        stat.setName("station");
        for (String station : stations) {
            
            stat.addNewAllowedValues().addNewValue().setStringValue(station);
        }
        
        
        OperationDocument.Operation gettask = operationMetadata.addNewOperation();
        gettask.setName("GetTask");
        gettask.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                            +"eosps?service=SPS&request=GetTask");
        DomainType gettaskprocedure = gettask.addNewConstraint();
        gettaskprocedure.setName("task");        
        OperationDocument.Operation getstat = operationMetadata.addNewOperation();
        getstat.setName("GetStatus");
        getstat.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                            +"eosps?service=SPS&request=GetStatus");
        DomainType getstatprocedure = gettask.addNewConstraint();
        getstatprocedure.setName("task");
        
        
        if (EOSPSFeasibilityWorker.class.isAssignableFrom(worker.getClass())) {
            
            OperationDocument.Operation getfeas = operationMetadata.addNewOperation();
            getfeas.setName("GetFeasibility");
            getfeas.addNewDCP().addNewHTTP().addNewPost().setHref(serverBaseURI + serviceBaseAddr
                                +"eosps?service=SPS&request=GetFeasibility");
            getfeas.addNewConstraint().addNewDataType().setStringValue("eosps:GetFeasibilityDocument");
        }
        
        if (EOSPSPlanningWorker.class.isAssignableFrom(worker.getClass())) {
            
            OperationDocument.Operation sub = operationMetadata.addNewOperation();
            sub.setName("Submit");
            sub.addNewDCP().addNewHTTP().addNewPost().setHref(serverBaseURI + serviceBaseAddr
                                +"eosps?service=SPS&request=Submit");
            sub.addNewConstraint().addNewDataType().setStringValue("eosps:SubmitDocument");
            
            OperationDocument.Operation dra=  operationMetadata.addNewOperation();
            dra.setName("DescribeResultAccess");
            dra.addNewDCP().addNewHTTP().addNewPost().setHref(serverBaseURI + serviceBaseAddr
                                +"/eosps?service=SPS&request=DescribeResultAccess");
            dra.addNewConstraint().addNewDataType().setStringValue("eosps:DescribeResultAccessDocument");
            
            OperationDocument.Operation val = operationMetadata.addNewOperation();
            val.setName("Validate");
            val.addNewDCP().addNewHTTP().addNewPost().setHref(serverBaseURI + serviceBaseAddr
                                +"eosps?service=SPS&request=Validate");
            val.addNewConstraint().addNewDataType().setStringValue("eosps:ValidateDocument");
        }
        
        if (EOSPSFeasibilityPlanningWorker.class.isAssignableFrom(worker.getClass())) {
            
            OperationDocument.Operation subSeg = operationMetadata.addNewOperation();
            subSeg.setName("SubmitSegmentByID");
            subSeg.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                                +"eosps?service=SPS&request=SubmitSegmentByID");
            DomainType subsegtask = subSeg.addNewConstraint();
            subsegtask.setName("task");
            DomainType subsegseg = subSeg.addNewConstraint();
            subsegseg.setName("segment");
        }
        
        if (EOSPSCancellationWorker.class.isAssignableFrom(worker.getClass())) {
            
            OperationDocument.Operation canc = operationMetadata.addNewOperation();
            canc.setName("Cancel");
            canc.addNewDCP().addNewHTTP().addNewGet().setHref(serverBaseURI + serviceBaseAddr
                                +"eosps?service=SPS&request=Cancel");
            DomainType canceltask = canc.addNewConstraint();
            canceltask.setName("task");
        }
        
        if (EOSPSReservationWorker.class.isAssignableFrom(worker.getClass())) {
            
            OperationDocument.Operation res = operationMetadata.addNewOperation();
            res.setName("Reserve");
            res.addNewDCP().addNewHTTP().addNewPost().setHref(serverBaseURI + serviceBaseAddr
                                +"eosps?service=SPS&request=Reserve");
            res.addNewConstraint().addNewDataType().setStringValue("eosps:ReserveDocument");

            OperationDocument.Operation upd = operationMetadata.addNewOperation();
            upd.setName("Update");
            upd.addNewDCP().addNewHTTP().addNewPost().setHref(serverBaseURI + serviceBaseAddr
                                +"eosps?service=SPS&request=Update");
            upd.addNewConstraint().addNewDataType().setStringValue("eosps:UpdateDocument");

            OperationDocument.Operation conf = operationMetadata.addNewOperation();
            conf.setName("Confirm");
            conf.addNewDCP().addNewHTTP().addNewPost().setHref(serverBaseURI + serviceBaseAddr
                                +"eosps?service=SPS&request=Confirm");
            conf.addNewConstraint().addNewDataType().setStringValue("eosps:ConfirmDocument");
        }
        
        return operationMetadata;
    }
}





