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

	<ctl:test name="reset:ATS_14.1.1.2_GET_Capabilities_ServiceIdentification">
    <ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
    <ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
    <ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
    <ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>    
		<ctl:assertion>To verify that he RESET Server under test correctly returns the ServiceIdentification section of a Capabilities resource.</ctl:assertion>
		<ctl:link title="14.1.1.2 GET Capabilities ServiceIdentification">http://www.opengis.net/spec/RESET/1.0/conf/Core/GET_Capabilities/ServiceIdentification</ctl:link>
    <ctl:code>

			<xsl:variable name="endpoint.identification" select="concat($sp.endpoint.HTTP.url, 'ServiceIdentification')"/>
			<xsl:message> <xsl:copy-of select="$endpoint.identification"/> </xsl:message>
			<xsl:variable name="identification.response">
        <ctl:request>
           <ctl:url>
            <xsl:copy-of select="$endpoint.identification"/>
           </ctl:url>
          <ctl:method>get</ctl:method>
            <parsers:XMLValidatingParser>
                  <parsers:schema type="file">
                    <xsl:value-of select="$cc.ows_schema_URL"/>              
                	</parsers:schema>
            </parsers:XMLValidatingParser>         
        </ctl:request>         
     </xsl:variable>       
     <xsl:if test="not ($identification.response/*)">
          <ctl:message>ATS 14.1.1.2 validation failed</ctl:message>
          <ctl:fail/>
     </xsl:if>   	
     </ctl:code>
	</ctl:test>
</ctl:package>
