Preparation of testing with ETS
===============================

This ETS currently supports for the HMA EO Satellite Tasking(OGC 14-012) the version 0.9.3. 
For test preparation follow these steps:
1-	Setup Test Environment:
2-	Copy the whole test scripts including ETS subdirecory to your system.
3-	Edit the main parameters:
4-	Start new test 


1- Setup Test Environment:
--------------------------
                a.  Download the last versione of TEAM-Engine from (svn checkout svn://svn.code.sf.net/p/teamengine/code/trunk teamengine-code)
                b.  Setup/Build TEAM-Engine v4, 
                                
                                How to build
                                ------------

                                Apache Maven 3.0 <http://maven.apache.org/> is required to build the teamengine 
                                code base, which consists of the following modules:

                                                * teamengine-core: Main CTL script processor
                                                * teamengine-console: Console (CLI) application
                                                * teamengine-web: Web application
                                                * teamengine-resources: Shared resources (stylesheets, schemas, etc.)
                                                * teamengine-realm: Custom Tomcat user realm
                                                * teamengine-spi: Extensibility framework and REST-like API


                                Simply run 'mvn package' in the root project directory to generate all build 
                                artifacts (using JDK 6 or later). Execute the 'mvn site' phase with the top-
                                level POM to generate project documentation; this will also create an aggregate 
                                PDF document i        <xsl:variable name="sp.endpoint.HTTP.url">http://localhost:8081/</xsl:variable>
        <!--xsl:variable name="sp.endpoint.HTTP.url">http://hma-s-dem-asu:8080/hmas_server-1.0-SNAPSHOT/hmas/</xsl:variable-->
        <xsl:variable name="cc.roseoschema_URL">/home/opgw/HMAS/ETS/schema/0.9.1/roseo.xsd</xsl:variable>
        <xsl:variable name="cc.ows_schema_URL">/home/opgw/HMAS/ETS/schema/ows/2.0/owsAll.xsd</xsl:variable>
         <xsl:variable name="cc.messagedir">/home/opgw/HMAS/ETS/netcatdemos</xsl:variable>n the target/pdf directory.


                                The main build artifacts are listed below.

                                                teamengine-console-${version}-bin.[zip|tar.gz]
                                                    The console application (command line usage)
                                                teamengine-console-${version}-base.[zip|tar.gz]
                                                    Contents of the main configuration directory (TE_BASE)
                                                teamengine.war
                                                    The JEE web (Servlet 2.5) application
                                                teamengine-common-libs.[zip|tar.gz]
                                                    Common runtime dependencies (e.g. JAX-RS 1.1, Apache Derby)


                                The value of the TE_BASE system property or environment variable specifies 
                                the location of the main configuration directory that contains several 
                                essential sub-directories. Unpack the contents of the *-base archive into 
                                the TE_BASE directory (for example, /home/opgw/HMAS/ETS).


2- Copy the whole test scripts including ETS subdirectory to your system. 
-------------------------------------------------------------------------

Copy the last RESET ETS *ctl scripts in the TE_BASE/scripts directory
                                TE_BASE/scripts/ETS.ctl
                                TE_BASE/scripts/Core
                                TE_BASE/scripts/Feasibility
                                TE_BASE/scripts/Planning

3. Edit the main parameters:
                In TE_BASE/scripts/ETS.ctl, edit the following parameters:
				
			<xsl:variable name="sp.endpoint.HTTP.url">http://127.0.0.1:8080/HMASResetServices/hmas/reset/1.0.0/</xsl:variable>
			<xsl:variable name="cc.resetschema_URL">C:\Java\team_engine\schema\reset\1.0\reset.xsd</xsl:variable>
			<xsl:variable name="cc.ows_schema_URL">C:\Java\team_engine\schema\ows\1.1.0\owsAll.xsd</xsl:variable>
			<xsl:variable name="cc.messagedir">C:/Java/team_engine/netcatdemos</xsl:variable>
			
				For the following ones, leave empty if you don't want to try this sensor type
				Or a valid sensor within your system
			
			<xsl:variable name="cc.procedure.opt"></xsl:variable>
			<xsl:variable name="cc.procedure.sar"></xsl:variable>

                Adapt test.bat/test.sh in TEAMEngine\bin to your needs: especially change the JAVA_OPTS values for http.proxyHost, http.proxyPort and http.nonProxyHosts to your needs.




4. Start new Test
-------------------

                To test the installation, go to teamengine-code/bin directory and run the file test.sh:
                
                ./test.sh [-mode=test] -source=ctlfile|dir [-source=ctlfile|dir] ...

                                  [-workdir=dir] [-logdir=dir] [-session=session] [-base=baseURI]

                                  [-suite=qname|-test=qname [@param-name=value] ...] [-profile=qname|*] ...

                                  qname=[namespace_uri,|prefix:]local_name]


                In our case:
                ./test.sh -source=reset/ETS.ctl 
                
                Before running a new test session, clean the environment from previous executions:
                go to ETS/work directory and clean the content (rm -fr *)

20-05-2014
Sebastian ULRICH (Astrium LTD)
