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

	<ctl:test name="reset:ATS_14.1.1.5_GET_Capabilities_non_nominal_conditions">
    <ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
    <ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
    <ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
    <ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>    
		<ctl:assertion>Verification that the RESET Server under test throws a correct error message when incorrect HTTP GET request of Capabilities resource is received</ctl:assertion>
		<ctl:link title="14.1.1.5 GET Capabilities/non_nominal_conditions">http://www.opengis.net/spec/RESET/1.0/conf/Core/GET_Capabilities-non-nominal</ctl:link>
    <ctl:code>

			<xsl:variable name="endpoint.nnc" select="concat($sp.endpoint.HTTP.url, 'WRONG_SECTION')"/>
			<xsl:message> <xsl:copy-of select="$endpoint.nnc"/> </xsl:message>
     <xsl:variable name="nnc.response">
        <ctl:request>
          <ctl:url>
            <xsl:copy-of select="$endpoint.nnc"/>
          </ctl:url>
          <ctl:method>get</ctl:method>
            <parsers:XMLValidatingParser>
                  <parsers:schema type="file">
                      <xsl:value-of select="$cc.ows_schema_URL"/>
                	</parsers:schema>
            </parsers:XMLValidatingParser>         
        </ctl:request>         
     </xsl:variable>       
     <xsl:if test="not ($nnc.response/*)">
          <ctl:message>ATS 14.1.1.5 validation failed</ctl:message>
          <ctl:fail/>
     </xsl:if>   	
     <!-- ############################################################################################# -->
     <!-- #includes an ows:ExceptionReport elements set as Table 7-9                                  # -->
     <!-- ############################################################################################# -->
      <xsl:if test="not($nnc.response//ows:Exception[@exceptionCode='InvalidParameterValue'][@locator='Name of parameter with invalid value']/ows:ExceptionText)">
				<ctl:message>ExceptionReport is not set as table 7-9 </ctl:message>
				<ctl:fail/>
			</xsl:if>
      <xsl:if test="$nnc.response//ows:Exception[@exceptionCode='InvalidParameterValue'][@locator='Name of parameter with invalid value']/ows:ExceptionText != 'Invalid value for Parameter'">
				<ctl:message>ExceptionReport is not set as table 7-9 </ctl:message>
				<ctl:fail/>
			</xsl:if>
      <xsl:if test="not($nnc.response//ows:Exception[@exceptionCode='NoApplicableCode']/ows:ExceptionText)">
				<ctl:message>ExceptionReport is not set as table 7-9 </ctl:message>
				<ctl:fail/>
			</xsl:if>
    </ctl:code>
	</ctl:test>
</ctl:package>
