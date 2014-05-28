<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:ctl="http://www.occamlab.com/ctl" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xi="http://www.w3.org/2001/XInclude" xmlns:saxon="http://saxon.sf.net/" xmlns:ows="http://www.opengis.net/ows/2.0" xmlns:ctlp="http://www.occamlab.com/te/parsers" xmlns:dct="http://purl.org/dc/terms/" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:parsers="http://www.occamlab.com/te/parsers" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:sec="http://www.intecs.it/sec" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:csw="http://www.opengis.net/cat/csw/2.0.2" xmlns:util="http://www.occamlab.com/te/util" xmlns:os="http://www.opengis.net/oseo/1.0" xmlns:oseo="http://www.opengis.net/oseo/1.0" xmlns:roseo="http://www.opengis.net/roseo/1.0" xmlns:osdd="http://a9.com/-/spec/opensearch/1.1/" xsi:schemaLocation="http://www.occamlab.com/ctl  ../apps/engine/resources/com/occamlab/te/schemas/ctl.xsd">
	<!-- CTL FILE METADATA
		Authors: Daniele Marchionni, TELESPAZIO (daniele.marchionni@telespazio.com)
		Melad Nassar      , TELESPAZIO (melad.nassar@external.telespazio.com)
		Lineage: 2013/11/28 - TELESPAZIO
  -->
	<ctl:test name="roseo:ATS_11.1.19_Get_Order_non_nominal_conditions">
		<ctl:param name="sp.endpoint.HTTP.url">HTTP URL of GetCapabilities</ctl:param>
		<ctl:param name="cc.roseoschema_URL">ROSEO Schema URL</ctl:param>
		<ctl:param name="cc.ows_schema_URL">OWS Schema URL</ctl:param>
		<ctl:param name="cc.messagedir">INPUT MESSAGES</ctl:param>
		<ctl:assertion>To verify whether the ROSEO Server under test is able to search Order resources by all criteria.</ctl:assertion>
		<ctl:link title="11.1.19 Get Order/non_nominal_conditions">http://www.opengis.net/spec/ROSEO/1.0/conf/Core/GET_Order-non-nominal</ctl:link>
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
			<xsl:variable name="xmlfile">ATS_11.1.11_Post_Order.xml</xsl:variable>
			<xsl:variable name="document">
				<xsl:value-of select="concat($cc.messagedir, '/', $xmlfile)"/>
			</xsl:variable>
			<xsl:variable name="orderID">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="concat($sp.endpoint.HTTP.url, '/order')"/>
					</ctl:url>
					<ctl:method>post</ctl:method>
					<ctl:header name="Content-type">application/xml</ctl:header>
					<ctl:body>
						<xsl:copy-of select="document($document)"/>
					</ctl:body>
				</ctl:request>
			</xsl:variable>
			<!-- #############################################################################################  -->
			<!-- # The returned element is valid against roseo.xsd XML Schema;                               #  -->
			<!-- #############################################################################################  -->
			<xsl:variable name="response">
				<ctl:request>
					<ctl:url>
						<xsl:copy-of select="concat($sp.endpoint.HTTP.url, '/order/','INCORRECT_ORDER_ID')"/>
					</ctl:url>
					<ctl:method>get</ctl:method>
					<parsers:XMLValidatingParser>
						<parsers:schema type="file">
							<xsl:value-of select="$cc.ows_schema_URL"/>
						</parsers:schema>
					</parsers:XMLValidatingParser>
				</ctl:request>
			</xsl:variable>
			<xsl:if test="not ($response/*)">
				<ctl:message>ATS 11.1.19 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<!-- ############################################################################################# -->
			<!-- #includes an ows:ExceptionReport elements set as Table 7-41                                  # -->
			<!-- ############################################################################################# -->
			<xsl:if test="not($response//ows:ExceptionReport/ows:Exception[@exceptionCode='InvalidParameterValue'][@locator='Name of parameter with invalid value']/ows:ExceptionText)">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="$response//ows:ExceptionReport/ows:Exception[@exceptionCode='InvalidParameterValue'][@locator='Name of parameter with invalid value']/ows:ExceptionText != 'Invalid value for Parameter'">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//ows:ExceptionReport/ows:Exception[@exceptionCode='NoApplicableCode']/ows:ExceptionText)">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<!-- #############################################################################################  -->
			<!-- # The returned element is valid against roseo.xsd XML Schema;                               #  -->
			<!-- #############################################################################################  -->
			<xsl:variable name="response">
				<ctl:request>
					<ctl:url>
						<xsl:copy-of select="concat($sp.endpoint.HTTP.url, '/order/',$orderID,'/','INCORRECT_ORDER_ITEM_ID')"/>
					</ctl:url>
					<ctl:method>get</ctl:method>
					<parsers:XMLValidatingParser>
						<parsers:schema type="file">
							<xsl:value-of select="$cc.ows_schema_URL"/>
						</parsers:schema>
					</parsers:XMLValidatingParser>
				</ctl:request>
			</xsl:variable>
			<xsl:if test="not ($response/*)">
				<ctl:message>ATS 11.1.19 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<!-- ############################################################################################# -->
			<!-- #includes an ows:ExceptionReport elements set as Table 7-41                                  # -->
			<!-- ############################################################################################# -->
			<xsl:if test="not($response//ows:ExceptionReport/ows:Exception[@exceptionCode='InvalidParameterValue'][@locator='Name of parameter with invalid value']/ows:ExceptionText)">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="$response//ows:ExceptionReport/ows:Exception[@exceptionCode='InvalidParameterValue'][@locator='Name of parameter with invalid value']/ows:ExceptionText != 'Invalid value for Parameter'">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//ows:ExceptionReport/ows:Exception[@exceptionCode='NoApplicableCode']/ows:ExceptionText)">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<!-- #############################################################################################  -->
			<!-- # The returned element is valid against roseo.xsd XML Schema;                               #  -->
			<!-- #############################################################################################  -->
			<xsl:variable name="response">
				<ctl:request>
					<ctl:url>
						<xsl:copy-of select="concat($sp.endpoint.HTTP.url, '/order','?','wrong_filter=100')"/>
					</ctl:url>
					<ctl:method>get</ctl:method>
					<parsers:XMLValidatingParser>
						<parsers:schema type="file">
							<xsl:value-of select="$cc.ows_schema_URL"/>
						</parsers:schema>
					</parsers:XMLValidatingParser>
				</ctl:request>
			</xsl:variable>
			<xsl:if test="not ($response/*)">
				<ctl:message>ATS 11.1.19 validation failed</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<!-- ############################################################################################# -->
			<!-- #includes an ows:ExceptionReport elements set as Table 7-41                                  # -->
			<!-- ############################################################################################# -->
			<xsl:if test="not($response//ows:ExceptionReport/ows:Exception[@exceptionCode='InvalidParameterValue'][@locator='Name of parameter with invalid value']/ows:ExceptionText)">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="$response//ows:ExceptionReport/ows:Exception[@exceptionCode='InvalidParameterValue'][@locator='Name of parameter with invalid value']/ows:ExceptionText != 'Invalid value for Parameter'">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
			<xsl:if test="not($response//ows:ExceptionReport/ows:Exception[@exceptionCode='NoApplicableCode']/ows:ExceptionText)">
				<ctl:message>ExceptionReport is not set as table 7-41</ctl:message>
				<ctl:fail/>
			</xsl:if>
		</ctl:code>
	</ctl:test>
</ctl:package>
