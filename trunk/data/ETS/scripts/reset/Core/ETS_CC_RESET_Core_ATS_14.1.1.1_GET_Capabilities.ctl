<?xml version="1.0" encoding="UTF-8"?>
<ctl:package 
		xmlns:ctl="http://www.occamlab.com/ctl" 
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
		xmlns:dc="http://purl.org/dc/elements/1.1/" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
		xmlns:xi="http://www.w3.org/2001/XInclude" 
		xmlns:saxon="http://saxon.sf.net/" 
		xmlns:ows="http://www.opengis.net/ows/1.1" 
		xmlns:ctlp="http://www.occamlab.com/te/parsers" 
		xmlns:dct="http://purl.org/dc/terms/" 
		xmlns:xs="http://www.w3.org/2001/XMLSchema"
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

	<ctl:test name="reset:ATS_14.1.1.1_GET_Capabilities">
    <ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
    <ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
    <ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
    <ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>    
		<ctl:assertion>This test to verify that he RESET Server under test correctly supports the HTTP GET method on Capabilities resource.</ctl:assertion>
		<ctl:link title="14.1.1.1 GET Capabilities">http://www.opengis.net/spec/RESET/1.0/conf/Core/GET_Capabilities</ctl:link>
    <ctl:code>
     <xsl:variable name="response">
        <ctl:request>
          <ctl:url>
            <xsl:copy-of select="$sp.endpoint.HTTP.url"/>
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
          <ctl:message>ATS 14.1.1.1 validation failed</ctl:message>
          <ctl:fail/>
     </xsl:if>   	
	 
     <!-- #############################################################################################  -->
     <!-- #Create the variables that will be used to test the sections of the Capabilities resource   #  -->
 <!-- #############################################################################################  -->
		<xsl:variable name="encoding" select="$response//reset:Capabilities//reset:Contents//sps:supportedEncoding"/>
		<xsl:variable name="profile" select="$response//reset:Capabilities//ows:ServiceIdentification//ows:Profile"/>
	 
     <!-- #############################################################################################  -->
     <!-- #check the ows:OperationsMetadata element is filled-in with the list of supported operations# -->
     <!-- #############################################################################################  -->
	 
  		<!--<xsl:if test="not($response//ows:OperationsMetadata/ows:Operation[@name='GetCapabilities']/ows:DCP/ows:HTTP/ows:Post/@xlink:href)">
				<ctl:message>GetCapabilities operation not supported</ctl:message>
				<ctl:fail/>
			</xsl:if>
      <xsl:if test="not($response//ows:OperationsMetadata/ows:Operation[@name='DescribeSensor']/ows:DCP/ows:HTTP/ows:Post/@xlink:href)">
				<ctl:message>GetOptions operation not supported</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//ows:OperationsMetadata/ows:Operation[@name='Submit']/ows:DCP/ows:HTTP/ows:Post/@xlink:href)">
				<ctl:message>Submit operation not supported</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//ows:OperationsMetadata/ows:Operation[@name='GetStatus']/ows:DCP/ows:HTTP/ows:Post/@xlink:href)">
				<ctl:message>GetStatus operation not supported</ctl:message>
				<ctl:fail/>
			</xsl:if>
			-->
     <!-- ################################################################################################################ -->
     <!-- #o	the Capabilities/ServiceIdentification/Profile element is set with: http://www.opengis.net/def/bp/reset/1.0# -->
     <!-- ################################################################################################################ -->
      
			<xsl:message> <xsl:value-of select="$profile"/> </xsl:message>
	  
	  <xsl:if test="$profile != 'http://www.opengis.net/reset/1.0'">
				<ctl:message>the Profile element is not set with the correct url</ctl:message>
				<ctl:fail/>
			</xsl:if>       
     <!-- ################################################################################################################ -->
     <!-- # The Capabilities/Contents/resourceURL shall report the supported resources                                   # -->
     <!-- ################################################################################################################ -->
      <xsl:if test="not($response//reset:resourceURL[@resource='capabilities']/@URL)">
				<ctl:message>the capabilities resource url is not supported</ctl:message>
				<ctl:fail/>
			</xsl:if>
      <xsl:if test="not($response//reset:resourceURL[@resource='procedures']/@URL)">
				<ctl:message>the procedures resource url is not supported</ctl:message>
				<ctl:fail/>
			</xsl:if>      
      <xsl:if test="not($response//reset:resourceURL[@resource='feasibility']/@URL)">
				<ctl:message>the feasibility resource url is not supported</ctl:message>
				<!--<ctl:fail/>    The feasibility resource is optional, so not implementing it does not mean that the implementation is wrong -->
			</xsl:if>   
      <xsl:if test="not($response//reset:resourceURL[@resource='planning']/@URL)">
				<ctl:message>the planning resource url is not supported</ctl:message>
				<!--<ctl:fail/>    The planning resource is optional, so not implementing it does not mean that the implementation is wrong -->
			</xsl:if>
			
      <xsl:if test="not($response//reset:resourceURL[@resource='reservation']/@URL)">
				<ctl:message>the reservation resource url is not supported</ctl:message>
				<!--<ctl:fail/>    The reservation resource is optional, so not implementing it does not mean that the implementation is wrong -->
			</xsl:if>   
     <!-- ################################################################################################################ -->
     <!-- # All attributes of Capabilities/Contents/SensorOfferingType element are present                               # -->
     <!-- ################################################################################################################ -->
      <xsl:if test="not($response//reset:Capabilities//reset:Contents//swes:offering//sps:SensorOffering//swes:procedure)">
				<ctl:message>The SensorOffering procedure element could not be found</ctl:message>
				<ctl:fail/>
		</xsl:if>
      <xsl:if test="not($response//reset:Capabilities//reset:Contents//swes:offering//sps:SensorOffering//swes:description)">
				<ctl:message>The SensorOffering description element could not be found</ctl:message>
				<ctl:fail/>
			</xsl:if>
      <xsl:if test="not($response//reset:Capabilities//reset:Contents//swes:offering//sps:SensorOffering//sps:observableArea)">
				<ctl:message>The SensorOffering observableArea element could not be found</ctl:message>
				<ctl:fail/>
			</xsl:if>
     <!-- ################################################################################################################ -->
     <!-- # The supported SWE encoding (Capabilities/Contents/ContentsType supportedEncoding) is XMLEncoding             # -->
     <!-- ################################################################################################################ -->
      
			<xsl:message> <xsl:value-of select="$encoding"/> </xsl:message>
			
			<xsl:if test="$encoding != 'http://www.opengis.net/swe/2.0/XMLEncoding'">
				<ctl:message>the supported SWE encoding is not set at http://www.opengis.net/swe/2.0/XMLEncoding </ctl:message>
				<!--<ctl:fail/>-->
			</xsl:if>
	  <xsl:if test="$encoding != 'http://www.opengis.net/swe/2.0/TextEncoding'">
				<ctl:message>the supported SWE encoding is not set at http://www.opengis.net/swe/2.0/TextEncoding </ctl:message>
				<!--<ctl:fail/>-->
			</xsl:if>
			
			
     <!-- ################################################################################################################ -->
     <!-- # Feasibility analysis is supported																             # -->
     <!-- ################################################################################################################ -->
      <xsl:if test="not($response/reset:Capabilities/reset:Contents//reset:SupportedClass/@resource='feasibility')">
				<ctl:message>Feasibility class is not implemented</ctl:message>
				<!-- <ctl:fail/> -->
			</xsl:if>
     <!-- ################################################################################################################ -->
     <!-- # Feasibility analysis planning is supported																             # -->
     <!-- ################################################################################################################ -->
      <xsl:if test="not($response/reset:Capabilities/reset:Contents//reset:SupportedClass/@resource='feasibilityplanning')">
				<ctl:message>Feasibility Planning class is not implemented</ctl:message>
				<!-- <ctl:fail/> -->
			</xsl:if>
     <!-- ################################################################################################################ -->
     <!-- # Planning is supported																             # -->
     <!-- ################################################################################################################ -->
      <xsl:if test="not($response/reset:Capabilities/reset:Contents//reset:SupportedClass/@resource='planning')">
				<ctl:message>Planning class is not implemented</ctl:message>
				<!-- <ctl:fail/> -->
			</xsl:if>
     <!-- ################################################################################################################ -->
     <!-- # Reservation is supported																             # -->
     <!-- ################################################################################################################ -->
      <xsl:if test="not($response/reset:Capabilities/reset:Contents//reset:SupportedClass/@resource='reservation')">
				<ctl:message>Reservation class is not implemented</ctl:message>
				<!-- <ctl:fail/> -->
			</xsl:if>
     <!-- ################################################################################################################ -->
     <!-- # Cancellation is supported																             # -->
     <!-- ################################################################################################################ -->
      <xsl:if test="not($response/reset:Capabilities/reset:Contents//reset:SupportedClass/@resource='cancellation')">
				<ctl:message>Cancellation class is not implemented</ctl:message>
				<!-- <ctl:fail/> -->
			</xsl:if>
    </ctl:code>
	</ctl:test>
</ctl:package>
