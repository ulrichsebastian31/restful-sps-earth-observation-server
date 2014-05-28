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
		xmlns:sml="http://www.opengis.net/sensorML/1.0.2"
 xsi:schemaLocation="http://www.occamlab.com/ctl  ../apps/engine/resources/com/occamlab/te/schemas/ctl.xsd">
	<!-- CTL FILE METADATA
  Author(s): Sebastian ULRICH  , Astrium LTD (sebastian.ulrich@astrium.eads.net)
  Lineage: 2014/05/01 - ASTRIUM
	-->
	<ctl:test name="reset:ATS_14.1.2.3_GET_Sensor_Tasking_Description">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:param name="cc.procedure">Procedure</ctl:param>
		<ctl:assertion>To verify whether the RESET Server under test provides description of a given sensor.</ctl:assertion>
		<ctl:link title="14.1.2.3 Get Sensor Description">http://www.opengis.net/spec/RESET/1.0/conf/Core/GET_Sensor_Description</ctl:link>
		<ctl:code>
			<!-- #############################################################################################  -->
			<!-- #
        Send the following request:
          o	HTTP Method: GET
          o	PATH: http://<hostname>:<port>/<context path>/RESET/1.0.0/procedures/{procedure identifier}/tasking
        The PATH shall be set using the <swes:procedure> provided in the ETS
 
        Verify the response message:
          o	HTTP Status: 200 OK
          o	HTTP Entity Body:
                o	Includes <eosps:DescribeTaskingResponse> element;
                o	The returned element is valid against eosps.xsd XML Schema;
                o	The returned <eosps:DescribeTaskingResponse> element includes all sections (§14.1.3), specifically:
                      <eosps:DescribeTaskingResponse>/eosps:CoverageProgrammingRequest/eosps:RegionOfInterest
                      <eosps:DescribeTaskingResponse>/eosps:CoverageProgrammingRequest/eosps:TimeOfInterest
                      <eosps:DescribeTaskingResponse>/eosps:CoverageProgrammingRequest/eosps:AcquisitionType
                      <eosps:DescribeTaskingResponse>/eosps:CoverageProgrammingRequest/eosps:ValidationParameters
			 -->
			 
			<!-- #############################################################################################  -->
			<!-- # The returned element is valid against reset.xsd XML Schema;                               #  -->
			<!-- #############################################################################################  -->
			
			<xsl:variable name="endpoint.description" select="concat($sp.endpoint.HTTP.url,'procedures/', $cc.procedure, '/tasking')"/>
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
				<ctl:message>ATS 14.1.2.3 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//eosps:CoverageProgrammingRequest//eosps:RegionOfInterest)">
				<ctl:message>The description does not contain an identification section</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//eosps:CoverageProgrammingRequest//eosps:TimeOfInterest)">
				<ctl:message>The description does not contain an validTime section</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//eosps:CoverageProgrammingRequest//eosps:AcquisitionType)">
				<ctl:message>The description does not contain an contact section</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//eosps:CoverageProgrammingRequest//eosps:ValidationParameters)">
				<ctl:message>The description does not contain an classification section</ctl:message>
				<ctl:fail/>
			</xsl:if>
		</ctl:code>
	</ctl:test>
</ctl:package>
