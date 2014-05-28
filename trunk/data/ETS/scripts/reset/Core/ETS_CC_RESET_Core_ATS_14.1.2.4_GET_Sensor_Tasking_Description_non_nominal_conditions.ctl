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
		xmlns:reset="http://www.opengis.net/reset/1.0" 
		xmlns:sml="http://www.opengis.net/sensorML/1.0.2"
 xsi:schemaLocation="http://www.occamlab.com/ctl  ../apps/engine/resources/com/occamlab/te/schemas/ctl.xsd">	<!-- CTL FILE METADATA
  Author(s): Sebastian ULRICH  , Astrium LTD (sebastian.ulrich@astrium.eads.net)
  Lineage: 2014/05/01 - ASTRIUM
  -->
	<ctl:test name="reset:ATS_14.1.2.4_GET_Sensor_Tasking_Description_non_nominal_conditions">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.resetschema_URL">RESET Schema URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:param name="cc.procedure">Procedure</ctl:param>
		<ctl:assertion>To verify whether the RESET Server under test provides ExceptionReport.</ctl:assertion>
		<ctl:link title="14.1.2.4 Get Sensor Description/non_nominal_conditions">
		http://www.opengis.net/spec/RESET/1.0.0/conf/Core/GET_Sensor_Tasking_Description-non-nominal
		</ctl:link>
		<ctl:code>
			<!-- #############################################################################################  -->
			<!-- #
          Send incorrect GET requests on Order resource:
              •	HTTP Method: GET
              •	PATH: http://<hostname>:<port>/<context path>/ROSEO/1.0.0/order
          Including several errors:
          1.	Incorrect Order identifier:
          PATH: http://<hostname>:<port>/<context path>/ROSEO/1.0.0/order/INCORRECT_ORDER_ID
          2.	Incorrect Order Item identifier:
          PATH: http://<hostname>:<port>/<context path>/ROSEO/1.0.0/order/{order identifier}/INCORRECT_ORDER_ITEM_ID
          3.	Incorrect Order Search criteria:
          PATH: http://<hostname>:<port>/<context path>/ROSEO/1.0.0/order?wrong_filter=100
          Verify the response message:
              •	HTTP Status: 400
              •	HTTP Entity Body:
            o	Includes an ows:ExceptionReport element set according to Table 7 41.
          Pass if the assertion is satisfied; fail otherwise.

                                                                                                       # -->
			<!-- #############################################################################################  -->
			
			<xsl:variable name="endpoint.description" select="concat($sp.endpoint.HTTP.url, 'procedures/', 'NON_VALID_SENSOR', '/tasking')"/>
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
				<ctl:message>ATS 14.1.2.4 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>
		</ctl:code>
	</ctl:test>
</ctl:package>
