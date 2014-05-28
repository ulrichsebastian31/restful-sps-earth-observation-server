/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.rs.parsers;

import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.parsers.describing.DescribeSensorParser;
import net.eads.astrium.hmas.rs.parsers.describing.DescribeTaskingParser;
import net.eads.astrium.hmas.rs.parsers.describing.GetCapabilitiesParser;
import net.eads.astrium.hmas.rs.parsers.describing.GetSensorAvailibilityParser;
import net.eads.astrium.hmas.rs.parsers.describing.GetStationAvailibilityParser;
import net.eads.astrium.hmas.rs.parsers.tasking.CancelParser;
import net.eads.astrium.hmas.rs.parsers.tasking.ConfirmParser;
import net.eads.astrium.hmas.rs.parsers.tasking.DescribeResultAccessParser;
import net.eads.astrium.hmas.rs.parsers.tasking.GetFeasibilityParser;
import net.eads.astrium.hmas.rs.parsers.tasking.GetStatusParser;
import net.eads.astrium.hmas.rs.parsers.tasking.GetTaskParser;
import net.eads.astrium.hmas.rs.parsers.tasking.ReserveParser;
import net.eads.astrium.hmas.rs.parsers.tasking.SubmitParser;
import net.eads.astrium.hmas.rs.parsers.tasking.SubmitSegmentByIDParser;
import net.eads.astrium.hmas.rs.parsers.tasking.UpdateParser;
import net.opengis.eosps.x20.CancelDocument;
import net.opengis.eosps.x20.ConfirmDocument;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStatusDocument;
import net.opengis.eosps.x20.GetTaskDocument;
import net.opengis.eosps.x20.ReserveDocument;
import net.opengis.eosps.x20.SubmitDocument;
import net.opengis.eosps.x20.SubmitSegmentByIDDocument;
import net.opengis.eosps.x20.UpdateDocument;
import net.opengis.sps.x21.DescribeResultAccessDocument;
import net.opengis.sps.x21.GetCapabilitiesDocument;
import net.opengis.sps.x21.GetCapabilitiesType;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author re-sulrich
 */
public class RestEOSPSParser implements RestGetParser,RestPostXMLParser{
    
    @Override
    public XmlObject createXMLFromGetRequest(String request, MultivaluedMap<String, String> params) {
        return validRequest(request, params, null);
    }

    @Override
    public XmlObject createXMLFromPostRequest(String request, String postRequest) {
        return validRequest(request, null, postRequest);
    }
    
    private static XmlObject validRequest(String request, MultivaluedMap<String, String> params, String postRequest) {
        
        XmlObject parameters = null;

        switch (request.toLowerCase()) {

            case "describetasking":
                DescribeTaskingDocument describeTasking = new DescribeTaskingParser().createXMLFromGetRequest(params);
                parameters = describeTasking;
            break;
            case "describesensor":
                DescribeSensorDocument describeSensor = new DescribeSensorParser().createXMLFromGetRequest(params);
                parameters = describeSensor;
            break;
            case "getsensoravailibility":
                GetSensorAvailabilityDocument getSensorAvailability = new GetSensorAvailibilityParser().createXMLFromGetRequest(params);
                parameters = getSensorAvailability;
            break;
            case "getstationavailibility":
                GetStationAvailabilityDocument getStationAvailability = new GetStationAvailibilityParser().createXMLFromGetRequest(params);
                parameters = getStationAvailability;
            break;
            case "getfeasibility":
                GetFeasibilityDocument getFeasibility = new GetFeasibilityParser().createXMLGetFeasibility(postRequest);
                parameters = getFeasibility;
            break;
            case "submit":
                SubmitDocument submit = new SubmitParser().createXMLSubmit(postRequest);
                parameters = submit;
            break;
            case "reserve":
                ReserveDocument reserve = new ReserveParser().createXMLReserve(postRequest);
                parameters = reserve;
            break;
            case "update":
                UpdateDocument update = new UpdateParser().createXMLUpdate(postRequest);
                parameters = update;
            break;
            case "confirm":
                ConfirmDocument confirm = new ConfirmParser().createXMLFromGetRequest(params);
                parameters = confirm;
            break;
            case "submitsegmentbyid":
                SubmitSegmentByIDDocument submitSegments = new SubmitSegmentByIDParser().createXMLFromGetRequest(params);

                String sync = params.getFirst("synchronous");
                if (sync != null) {
                    switch (sync.toLowerCase()) {
                        case "allsynchronous":
                            request += "as";
                        break;
                        case "allasynchronous":
                            request += "aa";
                        break;
                    }
                }
                
                parameters = submitSegments;
            break;
            case "cancel":
                CancelDocument cancel = new CancelParser().createXMLFromGetRequest(params);
                parameters = cancel;
            break;
            case "gettask":
                GetTaskDocument getTask = new GetTaskParser().createXMLFromGetRequest(params);
                parameters = getTask;
            break;
            case "getstatus":
                GetStatusDocument getStatus = new GetStatusParser().createXMLFromGetRequest(params);
                parameters = getStatus;

            break;
            case "describeresultaccess":
                DescribeResultAccessDocument describeResultAccess = new DescribeResultAccessParser().createXMLFromGetRequest(params);
                parameters = describeResultAccess;
            break;
            default:    //GetCapabilities
                request = "getcapabilities";
                GetCapabilitiesDocument doc = GetCapabilitiesParser.createXMLGetCapabilities(params);
                GetCapabilitiesType getCaps = doc.getGetCapabilities2();
                parameters = doc;
            break;
        }

        return parameters;
    }

}