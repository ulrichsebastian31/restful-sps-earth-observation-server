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
		xmlns:parsers="http://www.occamlab.com/te/parsers" 
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
	<!-- the list of Conformance classes 
      each Conformance Class is composed of a set of tests, each verifying one or more requirements of the corresponding Requirements Class
  -->
	<xi:include href="common.xml"/>
	<xi:include href="Core/ETS_CC_RESET_Core.ctl"/>
	<xi:include href="Feasibility/ETS_CC_RESET_Feasibility.ctl"/>
	<xi:include href="Planning/ETS_CC_RESET_Planning.ctl"/>
	
	<!-- ############################################################################################################ -->
	<!-- RESET Compliance CTL Test suite-->
	<!-- ############################################################################################################ -->
	<ctl:suite name="reset:compliance-suite">
		<ctl:title>OGC 14-012 (V 1.0.0) compliance test suite</ctl:title>
		<ctl:description>Validates an implementation claiming compliance with respect to the RESTful encoding of EO Satellite Tasking extension (14-012) Specification</ctl:description>
		<ctl:starting-test>reset:compliance-tests</ctl:starting-test>
	</ctl:suite>
	<!-- ############################################################################################################ -->
	<!-- RESET Compliance Tests implementation-->
	<!-- ############################################################################################################ -->
	<ctl:test name="reset:compliance-tests">
		<ctl:assertion>Run the tests on an OGC 14-012 ATS module basis</ctl:assertion>
		<ctl:comment>Complete set of tests for verifying the compliance of the Server Under Test with respect to the OGC RESTful encoding of EO Satellite Tasking extension (14-012) Specification </ctl:comment>
		<!--<ctl:link title="OGC 14-012 section 11">Annex A</ctl:link> -->
		<ctl:code>
			<xsl:variable name="form-values">
				<ctl:form height="500" width="800">
					<body>
						<h2 align="center">Compliance Test Suite of the OGC RESTful encoding of EO Satellite Tasking extension (14-012) Specification</h2>
						<h3 align="center">Input Parameters for the Executable Test Suite</h3>
						<table border="0" cellpadding="2" cellspacing="2" bgcolor="#E9CFEC">
							<tr>
								<td align="left">Endpoint Reference of the service provider</td>
								<td align="left">
									<input name="stringvalue.sp.endpoint.HTTP.url" size="70" type="text" value="http://hma-demo-opensearch.spacebel.be/HMASResetServices/hmas/reset/1.0.0/"/>
								</td>
							</tr>
							<tr>
								<td align="left">Reference of the sensor</td>
								<td align="left">
									<input name="stringvalue.cc.procedure" size="70" type="text" value="S1ASAR"/>
								</td>
							</tr>
							<tr>
								<td align="left">Reference of the sensor type</td>
								<td align="left">
									<input name="stringvalue.cc.sensor.type" size="70" type="text" value="SAR"/>
								</td>
							</tr>
						</table>
						<input style="text-align:center" type="submit" value="Start Test"/>
					</body>
				</ctl:form>
			</xsl:variable>
			<!-- ************************************************************************************************* -->
			<!-- DEFINITION OF COMMON PARAMETERS -->
			<xsl:variable name="sp.endpoint.HTTP.url" select="$form-values/values/value[@key='stringvalue.sp.endpoint.HTTP.url' ]"/>
			<xsl:variable name="cc.procedure" select="$form-values/values/value[@key='stringvalue.cc.procedure' ]"/>
			<xsl:variable name="cc.sensor.type" select="$form-values/values/value[@key='stringvalue.cc.sensor.type' ]"/>
			<!-- ************************************************************************************************* -->
					
			<!-- ############################################################################################################ -->
			<!-- DEFINITION OF COMMON INPUT PARAMETERS -->
			<!-- ############################################################################################################ -->
			<xsl:variable name="cc.resetschema_URL">/var/local/teamengine/scripts/reset/1.0/resources/schema/reset/1.0/reset.xsd</xsl:variable>
			<xsl:variable name="cc.ows_schema_URL">/var/local/teamengine/scripts/reset/1.0/resources/schema/ows/1.1.0/owsAll.xsd</xsl:variable>
			<xsl:variable name="cc.messagedir">/var/local/teamengine/scripts/reset/1.0/resources/netcatdemos</xsl:variable>
<!-- 
			<ctl:message></ctl:message>
			<ctl:message>The Input parameters are:                             </ctl:message>
			<ctl:message> the address of the service provider is: <xsl:value-of select="$sp.endpoint.HTTP.url"/>
			</ctl:message>
			<ctl:message> the URL to Reset schema is : <xsl:value-of select="$cc.resetschema_URL"/>
			</ctl:message>
			<ctl:message> the URL to OWS schema is : <xsl:value-of select="$cc.ows_schema_URL"/>
			</ctl:message>
			<ctl:message> the URL to input message directory is : <xsl:value-of select="$cc.messagedir"/>
			</ctl:message>
			<ctl:message> the OPT procedure to try is : <xsl:value-of select="$cc.procedure.opt"/>
			</ctl:message>
			<ctl:message> the SAR procedure to try is : <xsl:value-of select="$cc.procedure.sar"/>
			</ctl:message>
-->			
			<!-- ############################################################################################################ -->
<!--			
			<ctl:message></ctl:message>
			<ctl:message>Testing started at : <xsl:value-of select="current-dateTime()"/>
			</ctl:message>
			<ctl:message></ctl:message>
-->			
			<!-- ############################################################################################################ -->
			<!-- Conformance Classes -->
			<!-- ############################################################################################################ -->
<!--			
			<ctl:message>##############################################</ctl:message>
			<ctl:message>Section 14.1: Conformance Class Core </ctl:message>
			<ctl:message>##############################################</ctl:message>
-->			
			<ctl:call-test name="reset:RESET_Core_Conformance_Class">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
				<ctl:with-param name="cc.sensor.type" select="$cc.sensor.type"/>
			</ctl:call-test>
<!--			
			<ctl:message>##############################################</ctl:message>
			<ctl:message>Section 14.2: Conformance Class Feasibility </ctl:message>
			<ctl:message>##############################################</ctl:message>
-->			
			<ctl:call-test name="reset:RESET_Feasibility_Conformance_Class">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
				<ctl:with-param name="cc.sensor.type" select="$cc.sensor.type"/>
			</ctl:call-test>
<!--			
			<ctl:message>##############################################</ctl:message>
			<ctl:message>Section 14.3: Conformance Class Planning </ctl:message>
			<ctl:message>##############################################</ctl:message>
-->			
			<ctl:call-test name="reset:RESET_Planning_Conformance_Class">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
				<ctl:with-param name="cc.sensor.type" select="$cc.sensor.type"/>
			</ctl:call-test>
			<!--
			-->
		</ctl:code>
	</ctl:test>
</ctl:package>
