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
		xmlns:gml="http://www.opengis.net/gml/3.2" 
		xmlns:swes="http://www.opengis.net/swes/2.1" 
		xmlns:sps="http://www.opengis.net/sps/2.1" 
		xmlns:eosps="http://www.opengis.net/eosps/2.0" 
		xmlns:reset="http://www.opengis.net/reset/1.0" 
 xsi:schemaLocation="http://www.occamlab.com/ctl  ../apps/engine/resources/com/occamlab/te/schemas/ctl.xsd">
		<!-- CTL FILE METADATA
  Author(s): Sebastian ULRICH  , Astrium LTD (sebastian.ulrich@astrium.eads.net)
  Lineage: 2014/05/01 - ASTRIUM
 -->
	<xi:include href="ETS_CC_RESET_Planning_ATS_14.3.1.1_POST_Planning.ctl"/>
	<xi:include href="ETS_CC_RESET_Planning_ATS_14.3.1.2_GET_Task_Status.ctl"/>
	<xi:include href="ETS_CC_RESET_Planning_ATS_14.3.1.3_GET_Task.ctl"/>
	<xi:include href="ETS_CC_RESET_Planning_ATS_14.3.1.4_GET_Result_Access.ctl"/>
	<!--
	<xi:include href="ETS_CC_RESET_Planning_ATS_14.3.1.5_POST_Planning_non_nominal_conditions.ctl"/>
	<xi:include href="ETS_CC_RESET_Planning_ATS_14.3.1.6_GET_Task_Status_non_nominal_conditions.ctl"/>
	<xi:include href="ETS_CC_RESET_Planning_ATS_14.3.1.7_GET_Task_non_nominal_conditions.ctl"/>
	<xi:include href="ETS_CC_RESET_Planning_ATS_14.3.1.8_GET_Result_Access_non_nominal_conditions.ctl"/>
	-->
	
	<ctl:test name="reset:RESET_Planning_Planning_Tests">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:param name="cc.procedure">Procedure</ctl:param>
		<ctl:param name="cc.sensor.type">SensorType</ctl:param>
		<ctl:assertion>To verify whether the RESET Server under test implements the Planning resource.</ctl:assertion>
		<ctl:link title="14.3 Planning">http://www.opengis.net/spec/RESET/1.0/conf/Core/Planning</ctl:link>
		<ctl:code>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.3.1.1	POST Planning </ctl:message>
			<ctl:message>For procedure : <xsl:value-of select="$cc.procedure"/> </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.3.1.1_POST_Planning">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
				<ctl:with-param name="cc.sensor.type" select="$cc.sensor.type"/>
			</ctl:call-test>
		<!--
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.3.1.2	GET Task Status  </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.3.1.2_GET_Task_Status">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
				<ctl:with-param name="cc.sensor.type" select="$cc.sensor.type"/>
			</ctl:call-test>
			
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.3.1.3	GET Task </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.3.1.3_GET_Task">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
				<ctl:with-param name="cc.sensor.type" select="$cc.sensor.type"/>
			</ctl:call-test>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.3.1.4	GET Task Result Access </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.3.1.4_GET_Result_Access">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
				<ctl:with-param name="cc.sensor.type" select="$cc.sensor.type"/>
			</ctl:call-test>
			-->
			<!--
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.3.1.4	POST Planning non nominal conditions  </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.3.1.4_POST_Planning_non_nominal_conditions">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
				<ctl:with-param name="cc.sensor.type" select="$cc.sensor.type"/>
			</ctl:call-test>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.2.1.5	GET Task Status non nominal conditions  </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.2.1.5_GET_Task_Status_non_nominal_conditions">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>ATS section: 14.2.1.6	GET Task Status non nominal conditions    </ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:ATS_14.2.1.6_GET_Task_non_nominal_conditions">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
			-->
		</ctl:code>
	</ctl:test>
</ctl:package>
