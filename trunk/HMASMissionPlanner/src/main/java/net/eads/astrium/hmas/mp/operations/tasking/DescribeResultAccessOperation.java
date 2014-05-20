/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DescribeResultAccessOperation.java
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
 */package net.eads.astrium.hmas.mp.operations.tasking;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.dbhandler.tasking.SensorPlanningHandler;
import net.eads.astrium.hmas.exceptions.DescribeResultAccessFault;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import net.eads.astrium.hmas.util.structures.tasking.Product;
import net.eads.astrium.hmas.util.structures.tasking.Segment;
import net.opengis.ows.x11.AbstractReferenceBaseType;
import net.opengis.ows.x11.ReferenceGroupType;
import net.opengis.sps.x21.DataAvailableType;
import net.opengis.sps.x21.DataNotAvailableType;
import net.opengis.sps.x21.DescribeResultAccessDocument;
import net.opengis.sps.x21.DescribeResultAccessResponseDocument;
import net.opengis.sps.x21.DescribeResultAccessResponseType;
import net.opengis.sps.x21.DescribeResultAccessType;


/**
 * @file DescribeResultAccessOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The DescribeResultAccess Operation permits to task a satellite mission planner
 */
public class DescribeResultAccessOperation extends EOSPSOperation<MissionPlannerDBHandler,DescribeResultAccessDocument,DescribeResultAccessResponseDocument,DescribeResultAccessFault> {

    
	/**
	 * 
	 * @param request
	 */
	public DescribeResultAccessOperation(MissionPlannerDBHandler handler, DescribeResultAccessDocument request){

            super(handler,request);
	}

	@Override
	public void validRequest() throws DescribeResultAccessFault{
	}

	@Override
	public void executeRequest() throws DescribeResultAccessFault{
            
            DescribeResultAccessType.Target req = this.getRequest().getDescribeResultAccess().getTarget();
            String task = req.getTask();
            
            SensorPlanningHandler sensorPlanningHandler = this.getConfigurationLoader().getPlanningHandler();
            
            try {
                List<Segment> sensorSegments = sensorPlanningHandler.getSegments(task);
                
                Map<String,List<Product>> products = new HashMap<>();
                for (Segment segment : sensorSegments) {

                    List<Product> segmentProds = 
                            sensorPlanningHandler.getSegmentProducts(segment.getSegmentId());
                    
                    products.put(segment.getSegmentId(), segmentProds);
                }

                DescribeResultAccessResponseDocument responseDocument = DescribeResultAccessResponseDocument.Factory.newInstance();
                DescribeResultAccessResponseType resp = responseDocument.addNewDescribeResultAccessResponse();
                DescribeResultAccessResponseType.Availability availability = resp.addNewAvailability();

                //If the MMFAS task was not found (or no sensor tasks for this mmfas task, which should never happen)
                try {
                    this.validRequest();
                } catch (DescribeResultAccessFault e) {
                    if (e.getMessage().equals("MMFAS task not found.")) {

                        DataNotAvailableType dataNotAvailable = availability.addNewUnavailable().addNewDataNotAvailable();
                        dataNotAvailable.setUnavailableCode("1");
                        dataNotAvailable.addNewMessage().setStringValue("MMFAS task " + task + " not found.");
                    }
                }
                //If no products were found
                //Maybe they are not ready yet
                if (sensorSegments.isEmpty()) {
                    DataNotAvailableType dataNotAvailable = availability.addNewUnavailable().addNewDataNotAvailable();
                    dataNotAvailable.setUnavailableCode("2");
                    dataNotAvailable.addNewMessage().setStringValue("No data access found for task " + task + ".");
                }
                else {//List all the products available
                    //If some DAR has been created
                    List<DataAvailableType.DataReference> availables = new ArrayList<>();

                    if (products != null && !products.isEmpty()) {

                        for (String segmentId : products.keySet()) {
                            
                            //One per sensorTask
        //                    DataAvailableType.DataReference ref = dataAvailable.addNewDataReference();
                            DataAvailableType.DataReference ref = DataAvailableType.DataReference.Factory.newInstance();

                            //Sensor task ID ? dar ID ?
                            ReferenceGroupType grp = ref.addNewReferenceGroup();

                            grp.addNewIdentifier().setStringValue("SEGMENT_PRODUCTS_" + segmentId);
                            grp.addNewTitle().setStringValue("Description of result access for segment " + segmentId + " from sensor planning");

                            for (Product segmentProducts : products.get(segmentId)) {
                                //Product
                                AbstractReferenceBaseType refBase = grp.addNewAbstractReferenceBase();
                                refBase.setHref(segmentProducts.getDownloadURL());        //Product URL
                                refBase.setRole("PRODUCT_" + segmentProducts.getProductID());          //Product ID
                                refBase.setArcrole("SEGMENT_" + segmentProducts.getMmfasSegmentID());  //Segment ID
                            }

                            availables.add(ref);
                        }
                    }
                        
                    if (availables.isEmpty()) {
                        DataNotAvailableType dataNotAvailable = availability.addNewUnavailable().addNewDataNotAvailable();
                        dataNotAvailable.setUnavailableCode("3");
                        dataNotAvailable.addNewMessage().setStringValue("No data access is ready for task " + task + ".");
                    }
                    else {
                        DataAvailableType dataAvailable = availability.addNewAvailable().addNewDataAvailable();
                        dataAvailable.setDataReferenceArray(
                                availables.toArray(new DataAvailableType.DataReference[availables.size()])
                             );
                    }
                }
                
                this.setResponse(responseDocument);

            } catch (SQLException | ParseException ex) {
                Logger.getLogger(DescribeResultAccessOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}
