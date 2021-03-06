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
	<ctl:test name="reset:ATS_14.1.2.5_GET_Sensor_Availability_Description">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:param name="cc.procedure">Procedure</ctl:param>
		<ctl:assertion>To verify whether the RESET Server under test provides description of a given sensor.</ctl:assertion>
		<ctl:link title="14.1.2.5 Get Sensor Description">http://www.opengis.net/spec/RESET/1.0/conf/Core/GET_Sensor_Availability_Description</ctl:link>
		<ctl:code>
			<!-- #############################################################################################  -->
			<!-- #
        Send the following request:
          o	HTTP Method: GET
          o	PATH: http://<hostname>:<port>/<context path>/RESET/1.0.0/procedures/{procedure identifier}/availibility
        The PATH shall be set using the <swes:procedure> returned by test §14.1.2.
 
        Verify the response message:
          o	HTTP Status: 200 OK
          o	HTTP Entity Body:
                o	Includes <sml:SensorML> element;
                o	The returned element is valid against sensorML.xsd XML Schema;
                o	The ID of the returned order (<sml:SensorML//sml:member/@gml:id>) matches the one in the request;
                o	The returned <sml:SensorML> element includes all sections (§11.1.11), specifically:
                      <sml:SensorML>/sml:identification
                      <sml:SensorML>/sml:classification
                      <sml:SensorML>/sml:validTime
                      <sml:SensorML>/sml:contact
                o	The returned <sml:SensorML> element shall include all characteristics information:
                      <sml:SensorML>//characteristics/@xlin:role="urn:ogc:def:role:CEOS:eop:InstrumentConfigurations"
                      <sml:SensorML>//characteristics/@xlin:role="urn:ogc:def:role:CEOS:eop:MeasurementCharacteristics"
                      <sml:SensorML>//characteristics/@xlin:role="urn:ogc:def:role:CEOS:eop:GeometricCharacteristics"
                      <sml:SensorML>//characteristics/@xlin:role="urn:ogc:def:role:CEOS:eop:PhysicalCharacteristics"
			 -->
			 
			<!-- #############################################################################################  -->
			<!-- # The returned element is valid against reset.xsd XML Schema;                               #  -->
			<!-- #############################################################################################  -->
			
			<ctl:message> Getting date </ctl:message>
			
			<xsl:variable name="dateNow" select="current-dateTime()"/>
			<xsl:variable name="dateNowPlusWeek" select="current-dateTime()+7*xs:dayTimeDuration('P1D')"/>

			<xsl:variable name="dateFormat" select="format-dateTime($dateNow, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]Z')"/>
			<ctl:message> <xsl:copy-of select="$dateFormat"/> </ctl:message>
			
			<xsl:variable name="datePlusWeekFormat" select="format-dateTime($dateNowPlusWeek, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]Z')"/>
			<ctl:message> <xsl:copy-of select="$datePlusWeekFormat"/> </ctl:message>
			
			<xsl:variable name="endpoint.description" select="concat($sp.endpoint.HTTP.url,'procedures/', $cc.procedure, '/availibility', '?begin=', $dateFormat, '&amp;end=', $datePlusWeekFormat)"/>
			<xsl:message> <xsl:copy-of select="$endpoint.description"/> </xsl:message>
			<xsl:variable name="response">
				<ctl:request>
					<ctl:url>
						<xsl:copy-of select="$endpoint.description"/>
					</ctl:url>
					<ctl:method>get</ctl:method>
					<parsers:XMLValidatingParser>
						<parsers:schema type="file">
							<xsl:value-of select="$cc.resetschema_URL"/>
						</parsers:schema>
					</parsers:XMLValidatingParser>
				</ctl:request>
			</xsl:variable>
			<xsl:if test="not ($response/*)">
				<ctl:message>ATS 14.1.2.5 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>
			
			<xsl:if test="not($response//reset:responsePeriod//gml:TimePeriod[@gml:id='Response_Period'])">
				<ctl:message>The description does not contain a response period</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//reset:availabilityPeriod//gml:TimePeriod[@gml:id='Availability_Period'])">
				<ctl:message>The description does not contain an availability section</ctl:message>
				<ctl:fail/>
			</xsl:if>
		</ctl:code>
	</ctl:test>
</ctl:package>
