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
		xmlns:eop="http://earth.esa.int/eop"
		xmlns:sps="http://www.opengis.net/sps/2.1" 
		xmlns:eosps="http://www.opengis.net/eosps/2.0" 
		xmlns:reset="http://www.opengis.net/reset/1.0" 
 xsi:schemaLocation="http://www.occamlab.com/ctl  ../apps/engine/resources/com/occamlab/te/schemas/ctl.xsd">
 <!-- CTL FILE METADATA
  Author(s): Sebastian ULRICH  , Astrium LTD (sebastian.ulrich@astrium.eads.net)
  Lineage: 2014/05/01 - ASTRIUM
	-->
	<ctl:test name="reset:ATS_14.2.1.3_GET_Task">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:param name="cc.procedure">Procedure</ctl:param>
		<ctl:param name="cc.sensor.type">SensorType</ctl:param>
		<ctl:assertion>To verify whether the RESET Server under test is able to retrieve a previously created Feasibility resource.</ctl:assertion>
		<ctl:link title="14.2.1.3 Get Task">http://www.opengis.net/spec/RESET/1.0/conf/Feasibility/GET_Feasibility_Task/</ctl:link>
		<ctl:code>
			<!-- #############################################################################################  -->
			<!-- #
        Verify that the RESET Server under test accepts the following request:
			o	HTTP Method: GET
			o	PATH: 
			http://<hostname>:<port>/<context 
			path>/RESET/1.0.0/feasibility/{taskID}/segments
		Verify the response message:
			o	HTTP Status: 200 OK
			o	HTTP Entity Body:
			o	complies with the GetTaskResponse described in the EO-SPS specification [NR22];
			o	contains a StatusReport element as defined in RESET Feasibility Requirement Class section 9 above;
			o	StatusReport contains a “FEASIBILITY COMPLETED” status identifier;
			o	contains at least one FeasibilityStudy element as defined in RESET Feasibility Requirement Class section 9 above;
			o	Segment structures within the FeasibilityStudy have a “POTENTIAL” status.
			Pass if the assertion is satisfied; fail otherwise.


                                                                                                       # -->
			<!-- #############################################################################################  -->
			<!-- #############################################################################################  -->
			<!-- #  Create a feasibility through a POST request as done in 14.2.1.1                          #  -->
			<!-- #  And get the taskID 																	     #-->
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
					<sps:procedure><xsl:value-of select="$cc.procedure"/></sps:procedure>
					<xsl:copy-of select="document($document)"/>
				</eosps:GetFeasibility>
			</xsl:variable>

			<!--
			<xsl:message> Request is :  </xsl:message>
			<xsl:message> <xsl:copy-of select="$feasibility.request"/> </xsl:message>
			-->
	
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

			<!--
			<xsl:message> Response :  </xsl:message>
			<xsl:message>
				<xsl:copy-of select="$response"/>
			</xsl:message>
			-->
			
			<xsl:if test="not ($response/*)">
				<ctl:message>ATS 14.2.1.1 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>  

			<xsl:variable name="feasibility.task" select="$response//reset:Feasibility//reset:status//reset:status//sps:task"/>
			
			<xsl:message>Task ID : <xsl:copy-of select="$feasibility.task"/> </xsl:message>

			
			<!-- #############################################################################################  -->
			<!-- # Create the GetStatus request and send it						                             #  -->
			<!-- #############################################################################################  -->
			
			<xsl:variable name="endpoint.description" select="concat($sp.endpoint.HTTP.url,'feasibility/', $feasibility.task, '/segments')"/>
			<xsl:message>URL : <xsl:copy-of select="$endpoint.description"/> </xsl:message>
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
				<ctl:message>ATS 14.2.1.3 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>
			
			<xsl:message> Response :  </xsl:message>
			<xsl:message>
				<xsl:copy-of select="$response"/>
			</xsl:message>
			
			
			<!-- Status -->
			<xsl:variable name="resp.status" select="$response//reset:status//reset:status//sps:taskStatus"/>
			<xsl:variable name="resp.procedure" select="$response//reset:status//reset:status//sps:procedure"/>
			<xsl:variable name="resp.task" select="$response//reset:status//reset:status//sps:task"/>
			
			<xsl:if test="$resp.status != 'FEASIBILITY COMPLETED'">
				<ctl:message>the task status <xsl:copy-of select="$resp.status"/> is not valid.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<xsl:if test="$resp.procedure != $cc.procedure">
				<ctl:message>Procedures <xsl:copy-of select="$resp.procedure"/> and <xsl:copy-of select="$cc.procedure"/> do not match.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<xsl:if test="not($resp.task)">
				<ctl:message>the task ID was not found.</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="$resp.task != $feasibility.task">
				<ctl:message>Tasks <xsl:copy-of select="$resp.task"/> and <xsl:copy-of select="$feasibility.task"/> do not match.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<!-- Feasibility -->
			<xsl:variable name="resp.feasibility" select="$response//reset:Feasibility//reset:feasibility//reset:feasibility"/>
			<xsl:variable name="resp.segment" select="$resp.feasibility//eosps:segment"/>
			
			<xsl:if test="not($resp.feasibility)">
				<ctl:message>the FeasibilityStudy element was not found.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<xsl:if test="not($resp.segment)">
				<ctl:message>No Segment element was found.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<xsl:if test="not($resp.segment//eosps:status)">
				<ctl:message>The Segment element does not contain a status element.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<xsl:if test="$resp.segment//eosps:status != 'POTENTIAL'">
				<ctl:message>The Segment does not have the 'POTENTIAL' status.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<xsl:if test="not($resp.segment//eosps:footprint)">
				<ctl:message>The Segment element does not contain a footprint element.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<xsl:if test="not($resp.segment//eosps:acquisitionStartTime)">
				<ctl:message>The Segment element does not contain a acquisitionStartTime element.</ctl:message>
				<ctl:fail/>
			</xsl:if>

			<xsl:if test="not($resp.segment//eosps:acquisitionMethod//eop:EarthObservationEquipment)">
				<ctl:message>The Segment element does not contain an acquisition method element.</ctl:message>
				<ctl:fail/>
			</xsl:if>
			
		</ctl:code>
	</ctl:test>
</ctl:package>
