<?xml version="1.0" encoding="UTF-8"?>
<!--
Arif Shaon, STFC
-->
<ctl:package xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xi="http://www.w3.org/2001/XInclude" xmlns:ctl="http://www.occamlab.com/ctl" xmlns:saxon="http://saxon.sf.net/" xmlns:ctlp="http://www.occamlab.com/te/parsers" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0" xmlns:dct="http://purl.org/dc/terms/" xmlns:ows="http://www.opengis.net/ows/2.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:csw="http://www.opengis.net/cat/csw" xmlns:ebrimeo="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0" xmlns:eop="http://earth.esa.int/eop" xmlns:umeop="http://earth.esa.int/um/eop" xmlns:parsers="http://www.occamlab.com/te/parsers">

	<ctl:function name="umeop:verifyEncrptAlg">
		<ctl:param name="saml_token"/>
		<ctl:java class="uk.ac.stfc.ctl.SAMLAuthentication" method="verifyEncryptionAlg" initialized="true"/>
	</ctl:function>
	<ctl:function name="umeop:decrypt">
		<ctl:param name="auth_saml_token"/>
		<ctl:param name="key_id"/>
		<ctl:java class="uk.ac.stfc.ctl.SAMLAuthentication" method="decrypt" initialized="true">
			<ctl:with-param name="keystore_path" select="'c:/ESA/keystore.dat'"/>
			<ctl:with-param name="keystore_pass" select="'acl4242'"/>
			<ctl:with-param name="gpod_pub_key" select="'C:/ESA/gpod-cert.der'"/>
		</ctl:java>
	</ctl:function>
	<ctl:function name="umeop:validate_sign">
		<ctl:param name="dcrpt_saml_token"/>
		<ctl:java class="uk.ac.stfc.ctl.SAMLAuthentication" method="validateSignature" initialized="true">
			<ctl:with-param name="keystore_path" select="'c:/ESA/keystore.dat'"/>
			<ctl:with-param name="keystore_pass" select="'acl4242'"/>
			<ctl:with-param name="gpod_pub_key" select="'C:/ESA/gpod-cert.der'"/>
		</ctl:java>
	</ctl:function>
		<ctl:function name="umeop:encrypt_sign">
		<ctl:param name="saml_token"/>
		<ctl:param name="key_id"/>
		<ctl:java class="uk.ac.stfc.ctl.SAMLAuthentication" method="encrypt_sign" initialized="true">
			<ctl:with-param name="keystore_path" select="'c:/ESA/keystore.dat'"/>
			<ctl:with-param name="keystore_pass" select="'acl4242'"/>
			<ctl:with-param name="gpod_pub_key" select="'C:/ESA/gpod-cert.der'"/>
		</ctl:java>
	</ctl:function>
	<ctl:function name="umeop:verify_saml">
		<ctl:param name="dcrpt_saml_token"/>
		<ctl:java class="uk.ac.stfc.ctl.SAMLVerification" method="verifySAML" initialized="true">
		</ctl:java>
	</ctl:function>
	<ctl:function name="umeop:save_resp">
		<ctl:param name="resp"/>
		<ctl:param name="file_name"/>
		<ctl:java class="uk.ac.stfc.utils.Utils" method="saveResponse">
		</ctl:java>
	</ctl:function>
	
	<ctl:function name="umeop:sleep">
		<ctl:param name="interval"/>
		<ctl:java class="uk.ac.stfc.utils.Utils" method="sleep">
		</ctl:java>
	</ctl:function>
	
</ctl:package>
