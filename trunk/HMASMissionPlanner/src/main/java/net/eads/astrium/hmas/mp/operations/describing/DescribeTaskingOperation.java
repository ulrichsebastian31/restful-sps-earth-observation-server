/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DescribeTaskingOperation.java
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
 */package net.eads.astrium.hmas.mp.operations.describing;

import java.sql.SQLException;
import net.eads.astrium.hmas.operations.EOSPSOperation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import net.eads.astrium.dream.xml.generating.OGCNamespacesXmlOptions;
import net.eads.astrium.hmas.mp.database.MissionPlannerDBHandler;
import net.eads.astrium.hmas.util.structures.Sensor;
import net.eads.astrium.hmas.exceptions.DescribeTaskingFault;
import net.eads.astrium.hmas.util.DateHandler;

import net.opengis.eosps.x20.AcquisitionAngleType;
import net.opengis.eosps.x20.AcquisitionParametersOPTType;
import net.opengis.eosps.x20.AcquisitionParametersSARType;
import net.opengis.eosps.x20.AcquisitionParametersType;
import net.opengis.eosps.x20.AcquisitionTypeType;
import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseType;
import net.opengis.eosps.x20.IncidenceRangeType;
import net.opengis.eosps.x20.MonoscopicAcquisitionType;
import net.opengis.eosps.x20.RegionOfInterestType;
import net.opengis.eosps.x20.SurveyPeriodDocument;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.TimeOfInterestType;
import net.opengis.eosps.x20.ValidationParametersOPTType;
import net.opengis.eosps.x20.ValidationParametersSARType;
import net.opengis.eosps.x20.ValidationParametersType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.CircleType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingDocument;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.StringOrRefType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.swe.x20.AllowedTokensPropertyType;
import net.opengis.swe.x20.AllowedTokensType;
import net.opengis.swe.x20.AllowedValuesPropertyType;
import net.opengis.swe.x20.AllowedValuesType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.Reference;
import net.opengis.swe.x20.UnitReference;

/**
 * @file DescribeTaskingOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The DescribeTasking Operation gives information about the way the sensor can be tasked.
 *          
 *          This class is not currently working as some part of the response creation are not yet available.
 *          Plus, a lot of parameters should be found in a config file and not hard-coded.
 */
public class DescribeTaskingOperation extends EOSPSOperation<MissionPlannerDBHandler, DescribeTaskingDocument,DescribeTaskingResponseDocument, DescribeTaskingFault> {

	/**
	 * 
	 * @param request
	 */
	public DescribeTaskingOperation(MissionPlannerDBHandler loader, DescribeTaskingDocument request) {

            super(loader,request);
            
	}

	@Override
	public void validRequest() throws DescribeTaskingFault {
	}

	@Override
	public void executeRequest() throws DescribeTaskingFault {
		
		this.validRequest();
                
		DescribeTaskingResponseDocument responseDocument = DescribeTaskingResponseDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
		
		DescribeTaskingResponseType response = responseDocument.addNewDescribeTaskingResponse2();
                
//                response.addNewTaskingParameters().setName("EOSPS_Tasking_Parameters");
//                response.addNewExtension();
                
                TaskingParametersType taskingParam = response.addNewEoTaskingParameters();
                
		CoverageProgrammingRequestType coverage = this.getCoverageProgrammingRequest();
		
		taskingParam.setCoverageProgrammingRequest(coverage);

		this.setResponse(responseDocument);
	}

	/**
	 * Creates a coverage Programming request and fills it in with :
	 *  - an AcquisitionType, containing a set of Acquisition Parameters
	 *  - a Region of Interest
	 *  - a Time of Interest
	 *  - a set of Validation Parameters
	 * @return
	 */
	public CoverageProgrammingRequestType getCoverageProgrammingRequest() throws DescribeTaskingFault
	{
            CoverageProgrammingRequestType coverage = CoverageProgrammingRequestType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());

            coverage.setRegionOfInterest(this.getRegionOfInterest());

            coverage.setTimeOfInterest(this.getTimeOfInterest());
            
            String procedure = this.getRequest().getDescribeTasking().getProcedure();
            
            List<Sensor> sensors = null;
            
            try {
                if ("SAR OPT".contains(procedure))
                {
                    sensors = this.getConfigurationLoader().getSatelliteLoader().loadSensorsByType(procedure);
                }
                else
                {
                    sensors = new ArrayList<Sensor>();
                    
                    Sensor sensor = this.getConfigurationLoader().getSatelliteLoader().loadSensor(procedure);
                    if (sensor == null) {
                        throw new DescribeTaskingFault("Sensor " + procedure + " does not exist on this server.");
                    }
                    
                    sensors.add(sensor);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                
                throw new DescribeTaskingFault("SQLException ( " + ex.getErrorCode() + " ) : " + ex.getMessage());
            }
            
            
            String sensorType = "";
            if (sensors != null && sensors.size() > 0)
            {
                sensorType = sensors.get(0).getSensorType();
            }
            
            
            coverage.setAcquisitionType(this.getAcquisitionType(sensors, sensorType));

            coverage.setValidationParameters(this.getValidationParameters(sensorType));
            
            return coverage;
	}
	
	
	/**
	 * Creates a Time of Interest type
	 * @return
	 */
	private TimeOfInterestType getTimeOfInterest() {
		
		TimeOfInterestType toiType = TimeOfInterestType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
                SurveyPeriodDocument.SurveyPeriod surveyPeriod = toiType.addNewSurveyPeriod();
                TimePeriodType vt = surveyPeriod.addNewTimePeriod();
                vt.setId("Survey_Period");
                
                vt.addNewBeginPosition().setStringValue(DateHandler.formatDate(DateHandler.getCalendar().getTime()));
                vt.addNewEndPosition().setStringValue(DateHandler.formatDate(DateHandler.getCalendar().getTime()));
                
		return toiType;
	}

	/**
	 * Creates a Region of Interest type
	 * @return
	 */
	private RegionOfInterestType getRegionOfInterest() {
		

		CoordinatesType coords = CoordinatesType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
		coords.setDecimal(".");
		coords.setCs(",");
		coords.setTs(" ");
		
		RegionOfInterestType roiType = RegionOfInterestType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
		
		PolygonType polygon = roiType.addNewPolygon();
                polygon.setId("Region_Of_Interest");
		
		StringOrRefType def = StringOrRefType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
		def.setStringValue("" +
				"A Polygon is a special surface that is defined by a single surface patch (see D.3.6). " +
				"The boundary of this patch is coplanar and the polygon uses planar interpolation in its interior." +
				"The elements exterior and interior describe the surface boundary of the polygon.");
		
		polygon.setDescription(def);
		
		AbstractRingPropertyType exterior = polygon.addNewExterior();
		//TODO : Complete
		LinearRingDocument doc = LinearRingDocument.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
		LinearRingType lineRing = doc.addNewLinearRing();
		lineRing.setCoordinates(coords);
		
		exterior.set(doc);

		//TODO : Complete
		CircleType circle = roiType.addNewCircle();
		
		circle.setCoordinates(coords);
		//Complete polygon and circle structures here

		return roiType;
		
		
		// We don't have the right polygon structure
		
//		<swe:item name="Polygon">
//        <swe:DataRecord
//          definition="http://www.opengis.net/def/objectType/ISO-19107/2003/GM_Polygon">
//          <swe:field name="Exterior">
//            <swe:DataArray
//              definition="http://www.opengis.net/def/objectType/ISO-19107/2003/GM_Ring">
//              <swe:elementCount>
//                <swe:Count />
//              </swe:elementCount>
//              <swe:elementType name="Point">
//                <swe:Vector referenceFrame="http://www.opengis.net/def/crs/EPSG/0/4326">
//                  <swe:coordinate name="Lat">
//                    <swe:Quantity
//                      definition="http://www.opengis.net/def/property/OGC/0/GeodeticLatitude"
//                      axisID="Lat">
//                      <swe:uom code="deg" />
//                    </swe:Quantity>
//                  </swe:coordinate>
//                  <swe:coordinate name="Lon">
//                    <swe:Quantity
//                      definition="http://www.opengis.net/def/property/OGC/0/Longitude"
//                      axisID="Long">
//                      <swe:uom code="deg" />
//                    </swe:Quantity>
//                  </swe:coordinate>
//                </swe:Vector>
//              </swe:elementType>
//            </swe:DataArray>
//          </swe:field>
//        </swe:DataRecord>
//      </swe:item>
		
		
		
		
		//And we don't have the right Circle structure either
		
//      <swe:item name="Circle">
//        <swe:DataRecord
//          definition="http://www.opengis.net/def/objectType/ISO-19107/2003/GM_Circle">
//          <swe:field name="Center">
//            <swe:Vector referenceFrame="http://www.opengis.net/def/crs/EPSG/0/4326">
//              <swe:coordinate name="Lat">
//                <swe:Quantity
//                  definition="http://www.opengis.net/def/property/OGC/0/GeodeticLatitude"
//                  axisID="Lat">
//                  <swe:uom code="deg" />
//                </swe:Quantity>
//              </swe:coordinate>
//              <swe:coordinate name="Lon">
//                <swe:Quantity
//                  definition="http://www.opengis.net/def/property/OGC/0/Longitude"
//                  axisID="Long">
//                  <swe:uom code="deg" />
//                </swe:Quantity>
//              </swe:coordinate>
//            </swe:Vector>
//          </swe:field>
//          <swe:field name="Radius">
//            <swe:Quantity
//              definition="http://www.opengis.net/def/property/OGC-EO/0/Radius">
//              <swe:uom code="km" />
//            </swe:Quantity>
//          </swe:field>
//        </swe:DataRecord>
//      </swe:item>
		
		
	}

	/**
	 * Creates a Acquisition type
	 * @return
	 */
	public AcquisitionTypeType getAcquisitionType(List<Sensor> sensors, String sensorType) throws DescribeTaskingFault
	{
            AcquisitionTypeType acquisitionType = AcquisitionTypeType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());

            //TODO : Stereoscopic acquisitions
//            StereoscopicAcquisitionType stereo = acquisitionType.addNewStereoscopicAcquisition();

            MonoscopicAcquisitionType mono = acquisitionType.addNewMonoscopicAcquisition();

            //Fill in AcquisitionAngle type
            AcquisitionAngleType acquisitionAngle = mono.addNewAcquisitionAngle();

            
            IncidenceRangeType incidence = acquisitionAngle.addNewIncidenceRange();

            QuantityRangeType azimuth = incidence.addNewAzimuthAngle();

            azimuth.setDescription("Range of acceptable elevation incidence angles");
            azimuth.setLabel("Elevation Incidence Angle Range");

            UnitReference aziRef = UnitReference.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            aziRef.setCode("deg");

            azimuth.setUom(aziRef);

            ArrayList<String> aziValues = new ArrayList<String>();
            aziValues.add("-90");																					//config files
            aziValues.add("90");																					//config files

            azimuth.setValue(aziValues);

            AllowedValuesPropertyType aziAllowedValues = AllowedValuesPropertyType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            AllowedValuesType aziAllowed = aziAllowedValues.addNewAllowedValues();
            aziAllowed.addInterval(aziValues);

            azimuth.setConstraint(aziAllowedValues);

            QuantityRangeType elevation = incidence.addNewElevationAngle();

            elevation.setDescription("Range of acceptable elevation incidence angles");
            elevation.setLabel("Elevation Incidence Angle Range");

            UnitReference eleRef = UnitReference.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            eleRef.setCode("deg");

            elevation.setUom(eleRef);

            ArrayList<String> eleValues = new ArrayList<String>();
            eleValues.add("-90");																					//config files
            eleValues.add("90");																					//config files

            elevation.setValue(eleValues);

            AllowedValuesPropertyType eleAllowedValues = AllowedValuesPropertyType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            AllowedValuesType eleAllowed = eleAllowedValues.addNewAllowedValues();
            eleAllowed.addInterval(eleValues);

            elevation.setConstraint(eleAllowedValues);

            //Fill in AcquisitionParameters type
            AcquisitionParametersType acquisitionParameters = mono.addNewAcquisitionParameters();
            
            if (sensorType.equals("SAR"))
            {
                acquisitionParameters.setAcquisitionParametersSAR(createSARAcquisitionParameters(sensors));
            }
            if (sensorType.equals("OPT"))
            {
                acquisitionParameters.setAcquisitionParametersOPT(createOPTAcquisitionParameters());
            }
            																						//config files

            return acquisitionType;
	}
	
        public AcquisitionParametersOPTType createOPTAcquisitionParameters() {
            
            AcquisitionParametersOPTType optAcquisitionParameters = AcquisitionParametersOPTType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
            
            

            return optAcquisitionParameters;
        }
        
        public AcquisitionParametersSARType createSARAcquisitionParameters(List<Sensor> sensors) throws DescribeTaskingFault {
            
            List<String> res = new ArrayList<String>();
            for (Sensor sensor : sensors) {
                try {
                    res.addAll(
                            this.getConfigurationLoader().getSatelliteLoader().getResolutions(sensor.getSensorId(), "SAR")
                        );
                    
                } catch (SQLException ex) {
            ex.printStackTrace();
                    throw new DescribeTaskingFault("SQLException : contact your administrator.");
                }
            }
                    
            
            
            AcquisitionParametersSARType sarAcquisitionParameters = AcquisitionParametersSARType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());

            List<String> instrumentModes = new ArrayList<String>();
            List<String> polarisationModes = new ArrayList<String>();
            
            for (Sensor sensor : sensors) {
                
                Collection<String> ims = sensor.getInstrumentModes().values();
                for (String string : ims) {
                    if (!instrumentModes.contains(string))
                    {
                        instrumentModes.add(string);
                    }
                }
                
                Collection<String> pms = sensor.getPolarizationModes().values();
                for (String string : pms) {
                    if (!polarisationModes.contains(string))
                    {
                        polarisationModes.add(string);
                    }
                }
            }
            
            
            sarAcquisitionParameters.addNewFusionAccepted();
            
            QuantityRangeType groundResolution = sarAcquisitionParameters.addNewGroundResolution();
            UnitReference uom = groundResolution.addNewUom();
            uom.setCode("m");
            uom.setTitle("meters");
            
            
            AllowedValuesType allowedValues = groundResolution.addNewConstraint().addNewAllowedValues();
        
            for (String  d : res) {
                allowedValues.addNewInterval().setStringValue(d);
            }
            
            //Fill in InstrumentMode type in AcquisitionParameters type
            CategoryType instrumentMode = sarAcquisitionParameters.addNewInstrumentMode();

            instrumentMode.setLabel("Instrument Mode");

            Reference instrumentCodeSpace = instrumentMode.addNewCodeSpace();
            instrumentCodeSpace.setHref("http://www.esa.int/registry/ASARModes");

            AllowedTokensPropertyType instrumentConstraint = instrumentMode.addNewConstraint();
            AllowedTokensType instrumentAllowedTokens = instrumentConstraint.addNewAllowedTokens();
            
            //Add instrument modes for all the sensors
            for (String inst : instrumentModes) {
                
                instrumentAllowedTokens.addValue(inst);	
            }
            
//            instrumentAllowedTokens.addValue("SM");																					//config files
//            instrumentAllowedTokens.addValue("EW");																					//config files
//            instrumentAllowedTokens.addValue("IW");																					//config files
//            instrumentAllowedTokens.addValue("WV");																					//config files

            instrumentMode.setValue(instrumentModes.get(0));																							//config files

            //Fill in PolarizationMode type in AcquisitionParameters type
            CategoryType polarizationMode = sarAcquisitionParameters.addNewPolarizationMode();

            polarizationMode.setLabel("Polarization Mode");

            Reference polarizationCodeSpace = polarizationMode.addNewCodeSpace();
            polarizationCodeSpace.setHref("http://www.opengis.net/def/property/OGC-EO/0/sar/PolarizationModes");

            AllowedTokensPropertyType polarizationConstraint = polarizationMode.addNewConstraint();
            AllowedTokensType polarizationAllowedTokens = polarizationConstraint.addNewAllowedTokens();
            
            //Add polarisation modes for all the sensors
            for (String pol : polarisationModes) {
                
                polarizationAllowedTokens.addValue(pol);	
            }
            
//            polarizationAllowedTokens.addValue("HH");																					//config files
//            polarizationAllowedTokens.addValue("VV");																					//config files
//            polarizationAllowedTokens.addValue("HH/HV");																				//config files
//            polarizationAllowedTokens.addValue("VV/VH");																				//config files

            polarizationMode.setValue(polarisationModes.get(0));
            
            return sarAcquisitionParameters;
        }

    private ValidationParametersType getValidationParameters(String sensorType) {
        
        ValidationParametersType validationParameters = ValidationParametersType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        if (sensorType.equalsIgnoreCase("opt")) {
            validationParameters.setValidationParametersOPT(this.getOPTValidationParameters());
        }
        else {
            validationParameters.setValidationParametersSAR(this.getSARValidationParameters());
        }
        
        return validationParameters;
    }
        
    private ValidationParametersOPTType getOPTValidationParameters() {
        
        ValidationParametersOPTType valParams = ValidationParametersOPTType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        valParams.addNewMaxCloudCover().addNewUom().setCode("%");
        valParams.addNewMaxSnowCover().addNewUom().setCode("%");
        valParams.addNewMaxSunGlint().addNewUom().setCode("%");
        valParams.addNewHazeAccepted();
        valParams.addNewSandWindAccepted();
        
        return valParams;
    }   
    
    private ValidationParametersSARType getSARValidationParameters() {
        
        ValidationParametersSARType valParams = ValidationParametersSARType.Factory.newInstance(OGCNamespacesXmlOptions.getInstance());
        
        valParams.addNewMaxNoiseLevel().addNewUom().setCode("W");
        valParams.addNewMaxAmbiguityLevel().addNewUom().setCode("W");
        
        return valParams;
    }
}
