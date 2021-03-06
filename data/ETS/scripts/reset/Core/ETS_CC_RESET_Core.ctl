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
		xmlns:reset="http://www.opengis.net/reset/1.0" >
	
	<!-- CTL FILE METADATA
  Author(s): Sebastian ULRICH  , Astrium LTD (sebastian.ulrich@astrium.eads.net)
  Lineage: 2014/05/01 - ASTRIUM
  -->
  
	<xi:include href="ETS_CC_RESET_Core_Capabilities.ctl"/>
	<xi:include href="ETS_CC_RESET_Core_Procedures.ctl"/>
	
	<!-- ************************************************************************************************* 
   This Conformance Class is in charge of verifying the compliance of the RESET Server under test with 
       respect to the Core Requirement Class, which includes the basic functions that every RESET Server 
       shall implement i.e. feasibility analysis , sensor planning
       ************************************************************************************************* -->
	<ctl:test name="reset:RESET_Core_Conformance_Class">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.resetschema_URL">RESET SCHEMA URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS SCHEMA </ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:param name="cc.procedure">Procedure</ctl:param>
		<ctl:assertion>Tests for verifying the compliance with respect to Core Conformance Class</ctl:assertion>
		<ctl:link title="OGC 14-012 section 14">A.A</ctl:link>
		<ctl:code>
	<!--
	-->
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>Capabilities resource verification (ATS sections: 14.1.1)</ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:RESET_Core_Capabilities_Tests">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
			</ctl:call-test>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:message>Procedures resource verification (ATS sections: 14.1.2</ctl:message>
			<ctl:message>#######################################################################</ctl:message>
			<ctl:call-test name="reset:RESET_Core_Procedures_Tests">
				<ctl:with-param name="sp.endpoint.HTTP.url" select="$sp.endpoint.HTTP.url"/>
				<ctl:with-param name="cc.resetschema_URL" select="$cc.resetschema_URL"/>
				<ctl:with-param name="cc.ows_schema_URL" select="$cc.ows_schema_URL"/>
				<ctl:with-param name="cc.messagedir" select="$cc.messagedir"/>
				<ctl:with-param name="cc.procedure" select="$cc.procedure"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
</ctl:package>