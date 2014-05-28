<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:ctl="http://www.occamlab.com/ctl" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xmlns:dc="http://purl.org/dc/elements/1.1/" 
             xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
             xmlns:xi="http://www.w3.org/2001/XInclude" 
             xmlns:saxon="http://saxon.sf.net/" 
             xmlns:ows="http://www.opengis.net/ows/2.0" 
             xmlns:ctlp="http://www.occamlab.com/te/parsers" 
             xmlns:dct="http://purl.org/dc/terms/" 
             xmlns:xs="http://www.w3.org/2001/XMLSchema"
             xmlns:parsers="http://www.occamlab.com/te/parsers" 
             xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
             xmlns:sec="http://www.intecs.it/sec" 
             xmlns:xlink="http://www.w3.org/1999/xlink" 
             xmlns:csw="http://www.opengis.net/cat/csw/2.0.2" 
             xmlns:util="http://www.occamlab.com/te/util" 
			xmlns:gml="http://www.opengis.net/gml/3.2" 
			xmlns:swes="http://www.opengis.net/swes/2.1" 
			xmlns:eop="http://earth.esa.int/eop"
			xmlns:sps="http://www.opengis.net/sps/2.1" 
			xmlns:eosps="http://www.opengis.net/eosps/2.0" 
			xmlns:reset="http://www.opengis.net/reset/1.0" 
             xsi:schemaLocation="http://www.occamlab.com/ctl  ../apps/engine/resources/com/occamlab/te/schemas/ctl.xsd">

  <!-- CTL FILE METADATA
  Author(s): Sebastian ULRICH  , Astrium LTD (sebastian.ulrich@astrium.eads.net)
  Lineage: 2014/05/01 - ASTRIUM
  -->

	<ctl:test name="reset:ATS_14.2.1.4_POST_Feasibility_non_nominal_conditions">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:param name="cc.procedure">Procedure</ctl:param>
		<ctl:param name="cc.sensor.type">SensorType</ctl:param>
		<ctl:assertion>To verify whether the RESET Server under test is able to handle incorrect HTTP POST request to Feasibility resource.</ctl:assertion>
		<ctl:link title="11.2.4 Post Feasibility">http://www.opengis.net/spec/RESET/1.0/conf/Feasibility/POST_Feasibility-non-nominal</ctl:link>
    <ctl:code>
      
     <!-- #############################################################################################  -->
     <!-- #
        Send incorrect POST requests on Feasibility resource:
        •	HTTP Method: POST
        •	PATH: http://<hostname>:<port>/<context path>/RESET/1.0.0/feasibility
        Including several errors:
            1.	Feasibility element with a wrong tag e.g.: <reset:Feasibility>/<wrongTag>
            2.	Feasibility with with unsupported sensor: 
                a.	GetFeasibility/procedure = WRONG_SENSOR

                                                                                                       # -->
     <!-- #############################################################################################  -->
      
     <!-- #############################################################################################  -->
	 <!-- # Incorrect XML format																	  #  -->
     <!-- #############################################################################################  -->
			<xsl:variable name="feasibility.post.uri">
				<xsl:value-of select="concat($sp.endpoint.HTTP.url, 'feasibility')"/>
			</xsl:variable>       

			<xsl:variable name="xmlfile">
				<!--<xsl:value-of select="concat('ATS_14.2.1.1_POST_GetFeasibility_', $cc.sensor.type, '.xml')"/>-->
				<xsl:value-of select="concat('ATS_14.2.1.1_POST_TaskingParameters_', $cc.sensor.type, '.xml')"/>
			</xsl:variable>

			<xsl:variable name="document">
				<xsl:value-of select="concat($cc.messagedir, '/', $xmlfile)"/>
			</xsl:variable>

			<xsl:variable name="feasibility.request">
				<eosps:GetFeasibility xmlns:eosps="http://www.opengis.net/eosps/2.0" xmlns:sps="http://www.opengis.net/sps/2.1" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:swe="http://www.opengis.net/swe/2.0">
					<eosps:procedure>WRONG_SENSOR</eosps:procedure>
					<xsl:copy-of select="document($document)"/>
				</eosps:GetFeasibility>
			</xsl:variable>

			<!--
			-->
			<xsl:message> Request is :  </xsl:message>
			<xsl:message> <xsl:copy-of select="$feasibility.request"/> </xsl:message>
	
			<xsl:message>Procedure is : <xsl:copy-of select="$cc.procedure"/>
			</xsl:message>
			<xsl:message>Document is : <xsl:copy-of select="$document"/>
			</xsl:message>
			<xsl:message> Address is : <xsl:copy-of select="$feasibility.post.uri"/>
			</xsl:message>

			<xsl:variable name="response">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$feasibility.post.uri"/>
					</ctl:url>
					<ctl:method>post</ctl:method>
					<ctl:header name="Content-type">text/xml</ctl:header>
					<ctl:body>  
						<xsl:copy-of select="$feasibility.request"/>
					</ctl:body>
					<parsers:XMLValidatingParser>
						<parsers:schema type="file">
							<xsl:value-of select="$cc.resetschema_URL"/>
						</parsers:schema>
					</parsers:XMLValidatingParser>    
				</ctl:request>
			</xsl:variable> 

			<xsl:message> Response :  </xsl:message>
			<xsl:message>
				<xsl:copy-of select="$response"/>
			</xsl:message>

			<xsl:if test="not ($response/*)">
				<ctl:message>ATS 14.2.1.4 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>  
			
			<xsl:if test="not($response//ows:ExceptionReport)">
				<ctl:message>ExceptionReport is not set as table 7-9 </ctl:message>
				<ctl:fail/>
			</xsl:if>
     <!-- #############################################################################################  -->
	 <!-- # Incorrect Sensor																		  #  -->
     <!-- #############################################################################################  -->
			<xsl:variable name="incsens.feasibility.post.uri">
				<xsl:value-of select="concat($sp.endpoint.HTTP.url, 'feasibility')"/>
			</xsl:variable>       

			<xsl:variable name="incsens.xmlfile">
				<!--<xsl:value-of select="concat('ATS_14.2.1.1_POST_GetFeasibility_', $cc.sensor.type, '.xml')"/>-->
				<xsl:value-of select="concat('ATS_14.2.1.1_POST_TaskingParameters_', $cc.sensor.type, '.xml')"/>
			</xsl:variable>

			<xsl:variable name="incsens.document">
				<xsl:value-of select="concat($cc.messagedir, '/', $incsens.xmlfile)"/>
			</xsl:variable>

			<xsl:variable name="incsens.feasibility.request">
				<eosps:GetFeasibility xmlns:eosps="http://www.opengis.net/eosps/2.0" xmlns:sps="http://www.opengis.net/sps/2.1" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:swe="http://www.opengis.net/swe/2.0">
					<sps:procedure>WRONG_SENSOR</sps:procedure>
					<xsl:copy-of select="document($incsens.document)"/>
				</eosps:GetFeasibility>
			</xsl:variable>

			<!--
			-->
			<xsl:message> Request is :  </xsl:message>
			<xsl:message> <xsl:copy-of select="$feasibility.request"/> </xsl:message>
	
			<xsl:message>Procedure is : <xsl:copy-of select="$cc.procedure"/>
			</xsl:message>
			<xsl:message>Document is : <xsl:copy-of select="$document"/>
			</xsl:message>
			<xsl:message> Address is : <xsl:copy-of select="$feasibility.post.uri"/>
			</xsl:message>

			<xsl:variable name="response">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$feasibility.post.uri"/>
					</ctl:url>
					<ctl:method>post</ctl:method>
					<ctl:header name="Content-type">text/xml</ctl:header>
					<ctl:body>  
						<xsl:copy-of select="$feasibility.request"/>
					</ctl:body>
					<parsers:XMLValidatingParser>
						<parsers:schema type="file">
							<xsl:value-of select="$cc.resetschema_URL"/>
						</parsers:schema>
					</parsers:XMLValidatingParser>    
				</ctl:request>
			</xsl:variable> 

			<xsl:message> Response :  </xsl:message>
			<xsl:message>
				<xsl:copy-of select="$response"/>
			</xsl:message>

			<xsl:if test="not ($response/*)">
				<ctl:message>ATS 14.2.1.4 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>  
			
			<xsl:if test="not($response//ows:ExceptionReport)">
				<ctl:message>ExceptionReport is not set as table 7-9 </ctl:message>
				<ctl:fail/>
			</xsl:if>
    </ctl:code>
	</ctl:test>
</ctl:package>
