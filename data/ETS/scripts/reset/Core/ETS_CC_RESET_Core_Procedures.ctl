<?xml version="1.0" encoding="UTF-8"?>
<ctl:package 
		xmlns:ctl="http://www.occamlab.com/ctl" 
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
		xmlns:dc="http://purl.org/dc/elements/1.1/" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
		xmlns:xi="http://www.w3.org/2001/XInclude" 
		xmlns:saxon="http://saxon.sf.net/" 
		xmlns:ctlp="http://www.occamlab.com/te/parsers" 
		xmlns:dct="http://purl.org/dc/terms/" 
		xmlns:xs="http://www.w3.org/2001/XMLSchema" 
		xmlns:parsers="http://www.occamlab.com/te/parsers" 
		xmlns:ows="http://www.opengis.net/ows/1.1" 
		xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
		xmlns:sec="http://www.intecs.it/sec" 
		xmlns:xlink="http://www.w3.org/1999/xlink" 
		xmlns:csw="http://www.opengis.net/cat/csw/2.0.2" 
		xmlns:util="http://www.occamlab.com/te/util" 
		xmlns:swes="http://www.opengis.net/swes/2.1" 
		xmlns:sps="http://www.opengis.net/sps/2.1" 
		xmlns:eosps="http://www.opengis.net/eosps/2.0" 
		xmlns:reset="http://www.opengis.net/reset/1.0" 
 xsi:schemaLocation="http://www.occamlab.com/ctl  ../apps/engine/resources/com/occamlab/te/schemas/ctl.xsd">
		<!-- CTL FILE METADATA
  Author(s): Sebastian ULRICH  , Astrium LTD (sebastian.ulrich@astrium.eads.net)
  Lineage: 2014/05/01 - ASTRIUM
 -->
	<xi:include href="ETS_CC_RESET_Core_ATS_14.1.2.1_GET_Sensor_Description.ctl"/>
	<xi:include href="ETS_CC_RESET_Core_ATS_14.1.2.2_GET_Sensor_Description_non_nominal_conditions.ctl"/>
	<xi:include href="ETS_CC_RESET_Core_ATS_14.1.2.3_GET_Sensor_Tasking_Description.ctl"/>
	<xi:include href="ETS_CC_RESET_Core_ATS_14.1.2.4_GET_Sensor_Tasking_Description_non_nominal_conditions.ctl"/>
	<xi:include href="ETS_CC_RESET_Core_ATS_14.1.2.5_GET_Sensor_Availability_Description.ctl"/>
	<xi:include href="ETS_CC_RESET_Core_ATS_14.1.2.6_GET_Sensor_Availability_Description_non_nominal_conditions.ctl"/>
	
	<ctl:test name="reset:RESET_Core_Procedures_Tests">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:param name="cc.procedure">Procedure</ctl:param>
		<ctl:assertion>These tests verify the management of GET Sensor Description resources.</ctl:assertion>
		<ctl:link title="14.1.2 GET Sensor Description">http://www.opengis.net/spec/RESET/1.0/conf/Core/GET_Sensor_Description</ctl:link>
		<ctl:code>
		<!--
			-->
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.1.2.1	GET Sensor Description </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.1.2.1_GET_Sensor_Description">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.1.2.2	GET Sensor Description non nominal description  </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.1.2.2_GET_Sensor_Description_non_nominal_conditions">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
			
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.1.2.3	GET Sensor Tasking Description </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.1.2.3_GET_Sensor_Tasking_Description">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.1.2.4	GET Sensor Tasking Description non nominal description  </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.1.2.4_GET_Sensor_Tasking_Description_non_nominal_conditions">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.1.2.5	GET Sensor Availability Description </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.1.2.5_GET_Sensor_Availability_Description">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.1.2.6	GET Sensor Availability Description non nominal description  </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.1.2.6_GET_Sensor_Availability_Description_non_nominal_conditions">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
</ctl:package>
