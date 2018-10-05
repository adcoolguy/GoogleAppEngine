To Java 8 Runtime

- Currently you can not run this runtime locally with DEV server, thus you need to set your compiler to compile to Java 7
and set the JRE/JDK to 1.7 - run with r1.bat or j7 && r.

- You can only run Java 8 on production of GAEJ - run with r.bat.

- You can launch the enhancer accordingly by invoking e.cmd (run j7 or j8 if needed).

Google App Engine for Java (GAE4J) Showcase

How to launch?

cd bin
r

What URL?

http://localhost:8888

How to add users?

http://localhost:8888/userstart

This project demonstrates the following features on the Google App Engine for Java -

****** Major Highlights ******

Swagger UI 1.5.4
. http://localhost:8888/swagger/index.html

Spring MVC 4.0.7 Simplest Controller
. http://localhost:8888/web/hello

AngularJS Client that Consumes a Spring-based RESTful Web Service
. http://localhost:8888/html/hello.html
. It is adaptation from the source http://spring.io/guides/gs/consuming-rest-angularjs/
. 2Share sample YouTube based web app

Google Datastore API
. "Genius" Service
. Settings for site-specific changes (based on http://www.rominirani.com/category/cloud-computing/google-app-engine/)

Google JPA 2.0
. "Service Registry" http://localhost:8888/sr

Google JDO 2.0
. "Soapy Cleaning Portal" http://localhost:8888/sgc/jsp/main.jsp

XHR Level 2 (XHR2)
. Service Registry and Genius

AngularJS with Data-URI (data-ng-src)
. Use http://lightning.gs/ to optimize your png to save data store storage
. Please turn off PNG crush!

Elasticsearch Integration
. ElasticSearch 1.2
. Elastic Kibana 3.1.2.07
. Swagger for Elasticsearch (https://github.com/timschlechter/swagger-for-elasticsearch)

Genius Service
. To support delete action, add a Service Registry (SR) with a prefix "template:delete:[genius appId]" e.g.
template:delete:genius
template:delete:secured
template:delete:social
and the value of the SR entry will be the token (when a delete action is hit, a prompt will be shown "Please enter your token" to authorize further deletion)

Newer Versions of Web App Based on MV*
. ReactJS Sample App
. RiotJS Sample App

$$$$$$ Third-Party/OSS Integrations $$$$$$

. Tapestry 5.3.7
. Wicket 1.4.17 (Wicket 1.5.X will not work with GAEJ)
. Struts 2.2.3.1
. Sitemesh
. jQuery 1.3.2
. jQueryMobile 1.0
. Google's UserService, URLFetch and Calendar Service
. Spring Core 4.0.7
. Spring Security 3.2.5
. Spring Data JPA 1.3.0 (http://tommysiu.blogspot.com/2014/01/spring-data-on-gae-part-1.html and http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-eight-adding-functionality-to-a-repository/)
. Tapestry Spring Security (https://github.com/antalk/Tapestry-Spring-Security)
. POI
. Compass/Lucene 2.4.1 (JDO only)
. GAE4J's Task API 1.38
. GAE4J's Channel API 1.40
. GAE4J's Email API and in-bound service
. Apache HttpClient 4.1 (with modified ConnectionManager, org.esxx.js.protocol.GAEConnectionManager)
. Apache Wicket (with modified GAEModificationWatcher)
. AuDAO - SQL and Java DAO Generator (http://audao.spoledge.com/audao)
. OSCore (TextUtils.hyperlink)
. UrlRewriteFilter 3.2.0 (http://code.google.com/p/urlrewritefilter/)
. Simple CRUD service
. Tapestry5 CKEditor 3.6.2 (revision 7275) integration (https://github.com/plannowtech and http://ckeditor.com)
. Cloud Endpoints (thanks to https://groups.google.com/forum/?fromgroups=#!topic/endpoints-trusted-testers/VjmtMsY0zVM)
APIs exposed
. AngularJS 1.2.16 samples
. Bootstrap Icons (http://twitter.github.io/bootstrap/base-css.html#icons)
. zRSSFeed (http://www.zazar.net/developers/jquery/zrssfeed/)
. FullCalendar v1.6.4 (http://arshaw.com/fullcalendar/)
. jQuery Countdown Plugin v1.6.2 (http://www.keith-wood.name/countdown.html)
. Parse 1.3.4 SDK for user account management (https://parse.com/docs/js_guide#users and https://parse.com/docs/rest)
. Twitter 1.1 Samples (https://github.com/abraham/twitteroauth)
. Accordion With CSS3 (http://tympanus.net/codrops/2012/02/21/accordion-with-css3/)
. Store.js 1.3.14 (https://github.com/marcuswestin/store.js/)
. Ajax loader icons (http://ajaxload.info/)
. Page Piling (https://github.com/alvarotrigo/pagePiling.js)

https://[appid].appspot.com/_ah/api/explorer
https://localhost:8888/_ah/api/bkper/v1/temptransaction
https://localhost:8888/_ah/api/discovery/v1/apis

****** Costly Features ******

The following will allow one to determine, plan or/and avoid expensive operations related to a specific features:

1. SR Lucene Indexing (designed to be invoked manually vs originally automated for each create/update)
2. SR Lucene Search (protected operation with Spring Security)
3. 2Share Recurring Calendar Events (currently restricted only up to 6)
4. 2Share SHA-256 Hash Generation of Private/Public Resource (can be disabled via Google Doc's spreadsheet)

****** Parse

Sample objectId: 70p4Mz1tiY (pub) ahcnqmzCDR (ie) sTRc7XPuLs (j)
c.f. https://dashboard.parse.com/apps/2share/browser/_User

http://toshare.herokuapp.com/parse

https://devcenter.heroku.com/articles/deploying-a-parse-server-to-heroku

****** Twitter
Incomplete c.f.
https://github.com/abraham/twitteroauth/blob/master/test.php

****** jQuery Countdown with Time

http://stackoverflow.com/questions/1467647/jquery-countdown-plugin-not-accepting-time-as-well-as-date

****** HTML 5 video
Disabling discussion http://my.opera.com/community/forums/topic.dml?id=1081282

****** FullCalendar

For eventDrop to work (as at 1.6.2), the fullcalendar.js was changed i.e.

        //=== fixed suggested by https://code.google.com/p/fullcalendar/issues/detail?id=1208
		//var newCell = coordinateGrid.cell(ev.pageX, ev.pageY);
        var newCell = coordinateGrid.cell(ev.originalEvent.pageX || ev.pageX, ev.originalEvent.pageY || ev.pageY);

If you upgrade the FullCalendar, bear in mind that you possibly need to change the above again to make it work!

****** AngularJS

To support IE, please make sure you review http://docs.angularjs.org/guide/ie.

Do not forget to make sure the endpoint is added as parameter of SystemServiceServlet in web.xml/web.xml.* e.g.

   <param-value>app.model.CalendarEndpoint,com.bkper.server.api.TransactionEndpoint</param-value>

This is especially true if you are using deploy_all/deploy_core/deploy_core_appstats/deploy_struts/deploy_wicket scripts.
 
****** Simple CRUD Service

At least in servlet 2.5, you need to add your object handler in web.xml e.g.

	<listener>
		<listener-class>app.controller.CalendarHandler</listener-class>
	</listener>

c.f. https://blogs.oracle.com/swchan/entry/servlet_3_0_annotations.

****** XHR2 featues used in this project -

. Cross-origin
. Form-data

To test the above features, make sure your browser support them 
(c.f. http://tiffanybbrown.com/presentations/2011/xhr2/#xhr_advantages).

It also makes use of the following tool to generate some of the codes -

****** AuDAO - SQL and Java DAO Generator (http://audao.spoledge.com/)


****** Useful Utility Project

https://gae-datastore-migrator.googlecode.com/svn/trunk/java

Use the tools to import/export data from GAEJ datastore. 
It depends on appengine-remote-api.jar of GAEJ (e.g. add it from appengine-java-sdk-1.8.8\lib).

****** Poor Man's Caching of Datastore Calls

Service Registry's ServiceRegistryDAO.clearCache() in the countHit() method, as well as
updateCache() if it helps.

2Share's CacheManager dealing with allMoviesCache in doGetItems() etc.

****** Miscellaneous

SR hit count is disabled by default. To enable it, do:

. To apply a site specific secret key, say 1234567890, invoke:
http://localhost:8888/postsettings?setting=true&pincode=sr.hit.count.enabled
. To view it:
http://localhost:8888/settings?pincode=sr.hit.count.enabled


Also in development (not fully working, yet), using -

. Gig for Scaffolding (http://code.google.com/p/gig/wiki/CRUDScaffolding)
. StrutsTool (http://code.google.com/p/strutstool/)

What you need to know -

. If you are using Tapestry, and would like to use jQuery, do not forget to use jQuery() instead of $() to avoid conflict with Prototype.

. To avoid "java.lang.RuntimeException: Unable to restore the previous TimeZone" exception during startup, you need to add a VM parameter.

In Eclipse, go to "Run Configuration", under the "Arguments" tab add "-Dappengine.user.timezone=UTC" to the VM arguments.

http://code.google.com/p/googleappengine/issues/detail?id=6928

. To see a sample of catching OverQuotaException, you can refer to com.appspot.cloudserviceapi.sci.services.ServiceRegistryServlet.java.

. To customize a site specific look and feel or settings, use the Settings facilities.

1. To save settings, use e.g.

http://localhost:8888/postsettings?setting=10.178.12.176&pincode=backup.service.ip
http://localhost:8888/postsettings?setting=0&pincode=service.registry.count

2. To view a setting, use e.g.

http://localhost:8888/settings?type=COUNT_CURRENT_MONTH&setting=Header+Title&pincode=ALL

or just

http://localhost:8888/settings?pincode=home.page.title
http://localhost:8888/settings?pincode=service.registry.count

As example, for Parse SDK master key setup:

To set
http://localhost:8888/postsettings?setting=secret123&pincode=parse.master.key

http://localhost:8888/postsettings?setting=appId123&pincode=parse.app.id

To view
http://localhost:8888/settings?pincode=parse.master.key

http://localhost:8888/settings?pincode=parse.app.id

App Id Mask

. To mask an app id in Service Registry, add an entry "appidmask" (c.f. app.common.APP_ID_MASK)
. SR will replace all hyperlink of the service's appid with the mask

Logging

. Default logging level have been changed from WARNING to INFO (in logging.properties).
. To download/upload any kind, you need to know the following -
1. Need to create the configuration (in yaml) by running create_config.sh. 
2. Need to change
  connector: # TODO: Choose a connector here: csv, simplexml, etc...
to
  connector: csv # TODO: Choose a connector here: csv, simplexml, etc...
Otherwise, you will encounter the following error -
"google.appengine.api.yaml_errors.EventError: Unable to assign value 'None' to attribute 'connector':
Missing value is required."

  property_map:
    - property: __key__
      external_name: key
      export_transform: transform.key_id_or_name_as_string
to
  property_map:
    - property: id
      external_name: key
      export_transform: transform.key_id_or_name_as_string
      import_transform: transform.create_foreign_key('Kind', key_is_id=True)
If you didn't make the above change, all your entities will have 0 as the Ids and you NEED to purge all of them
BEFORE uploading again to avoid duplicates!

3. Make sure that .yaml and .csv files are matching e.g.
--config_file=download_kind2.yaml --filename=download_kind2.csv
Otherwise you will get 
KeyError: '[Your kind name]'

Configuration Tips -

2Share

. To turn off cache, visit the spreadsheet e.g. https://docs.google.com/spreadsheets/d/1nZ5bITADCBoiUXnExeCy1ejaJOZBj6eyHwiuP1409aY/edit#gid=0
and clear the entry:
cache	proxy	host	rest	url

. To turn on backup service
http://localhost:8888/postsettings?setting=true&pincode=advanced.ui.active
http://localhost:8888/postsettings?setting=true&pincode=sr.backup.service.enabled
. Known Issue:
Ocassional error: com.google.api.server.spi.config.ApiConfigException: class cloudserviceapi.app.controller.CrudService has no @Api annotation.

Development Tips -

. If you use JSTL, please do not forget to turn on the tag support with <%@page isELIgnored="false" %> as it is turned off by default.
. JSTL conflicts: please do not put any jstl library jar file in /war/WEB-INF/lib (e.g. jstl-api-1.2.jar), since this will cause a conflict with jstl jar used internally by GAE (thanks to the tips
written by http://charlie.cu.cc/2011/10/spring-mvc-google-app-engine-part-5/). selenium-server-standalone-2.35.0.jar seems to cause some conflicts too.
So please remove that as well to avoid any JSP error.
. Make use of src/log4j.properties (or war/WEB-INF/logging.properties) for your troubleshooting. They help!
. Goto http://localhost:8888/_ah/admin for the local dev server's data viewer.
. If you are using JPA, remember to update persistence.xml for the class involved as "exclude-unlisted-classes" is set to true.
. If you are using JDO, do not forget to update Project Settings -> Google -> App Engine -> ORM (c.f. http://cnapagoda.blogspot.com/2011/05/google-app-engine-datanucleus-does-not.html).
. If you are using Tapestry 5.2+, remember to 
- update AppModule.java to bind your implementation to the interface
- rename ExceptionReport.java temporarily/in dev for detailed exception
- add the child in contributeDefaultDataTypeAnalyzer() of AppModule for beaneditform to "see" the child
. If you have static resource to be deployed on the cloud, remember to add the resource into appengine-web.xml.
. If you are indexing for a search, remember to 
- add the domain object into the init method of Compass class
- annotate the searchable class with @Searchable
- annotate the class's properties with @SearchableId
- add the searchable property (the one which was annotated with @SearchableProperty) into CompassMultiPropertyQueryStringBuilder in the view e.g. search_*.jsp *** THIS IS IMPORTANT: missing this step, the property would not be part of the search!!! ***
. If you encounter weird or strange error, make sure that the JDK entry entry are on the top of the classpath hierachy e.g.
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/jdk1.6.0_23"/>
followed by app engine SDK entry e.g.
	<classpathentry kind="con" path="com.google.appengine.eclipse.core.GAE_CONTAINER/appengine-java-sdk-1.4.0"/>
Otherwise, one will encounter JSP compilation exception due to annotation error.
. If you encounter "org.datanucleus.store.appengine.DatastoreFieldManager.checkAssignmentToNotNullField" exception e.g.
org.compass.gps.device.jdo.JdoGpsDevice doIndex: {appengine2}: Failed to index the database
java.lang.NullPointerException: Datastore entity with kind Geniu and key Geniu(6) has a null property named frequencyCount.  This property is mapped to com.appspot.cloudserviceapi.dto.Geniu.frequencyCount, which cannot accept null values.
	at org.datanucleus.store.appengine.DatastoreFieldManager.checkAssignmentToNotNullField(DatastoreFieldManager.java:427)
Make sure your attribute is of object type like Integer instead of int.

. If you use persistence (which is mostly the case), make sure on the fly datanucleus compilation is working. Example good message is as the following -
DataNucleus Enhancer (version 1.1.4) : Enhancement of classes
DataNucleus Enhancer completed with success for 11 classes. Timings : input=718 ms, enhance=157 ms, total=875 ms. Consult the log for full details
. The following console message should be seen if everything works fine -
Jan 4, 2011 4:52:57 PM com.google.apphosting.utils.jetty.JettyLogger info
INFO: Logging to JettyLogger(null) via com.google.apphosting.utils.jetty.JettyLogger
Jan 4, 2011 4:53:00 PM com.google.apphosting.utils.config.AppEngineWebXmlReader readAppEngineWebXml
INFO: Successfully processed C:\Documents and Settings\ADMIN\Workspaces\Eclipse 3.5 Java\GoogleAppEngine\war\WEB-INF/appengine-web.xml
Jan 4, 2011 4:53:00 PM com.google.apphosting.utils.config.AbstractConfigXmlReader readConfigXml
INFO: Successfully processed C:\Documents and Settings\ADMIN\Workspaces\Eclipse 3.5 Java\GoogleAppEngine\war\WEB-INF/web.xml
log4j:WARN No appenders could be found for logger (org.springframework.web.filter.DelegatingFilterProxy).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
XSS filter (c) Coldbeans mailto:info@servletsuite.com ver. 1.1
AppModule bind() done.
AppModule contributeApplicationDefaults() done.
Jan 4, 2011 11:53:04 AM com.google.appengine.tools.development.DevAppServerImpl start
INFO: The server is running at http://localhost:8888/

. If you are running Spring, these 4 files have to be deleted from war/WEB-INF/lib -
datanucleus-appengine-1.0.X.final.jar
datanucleus-core-1.1.5.jar
datanucleus-enhancer-1.1.4.jar
datanucleus-jpa-1.1.5.jar

Otherwise, you will encounter cryptic errors once it is deployed like the timeout for no obvious reason.

. If you encounter the following error -
"CreateProcess error=87, The parameter is incorrect."
as specified like the one at http://code.google.com/p/google-web-toolkit/issues/detail?id=4395,
please make sure you check out the project without any subdirectory e.g. c:\GoogleAppEngine.

. If you are experiencing "This webpage is not available
The webpage at http://localhost:8888/app/xxxstart might be temporarily down or it may have moved permanently to a new web address." tapestry error, please make sure -
1. Your mapping in Sitemesh's decorators.xml or Spring's security-context.xml are correct
2. Your implementation are properly mapped in AppModule.java (Google Guice style)

. Please make sure that the domain object is specified in persistence.xml if -
a. If you experiencing an unexplained NPE with JPA
b. Unexplained/unrelated (i.e. complaining about domain object other than the correct one) "Caused by: java.lang.IllegalArgumentException: can't operate on 
multiple entity groups in a single transaction. found both Element {..." exception

. For ultimate purge on any kind (using low level datastore delete service), please invoke http://localhost:8888/nuke?kind=<NAME OF KIND> e.g.
http://localhost:8888/nuke?kind=Activity

. If your changes gave NPE due to the fact it is a GAEJ bug for not able to detect existing entity/row that does not contains a newly added column,
check out a suggested workaround tagged with BTXXXX e.g. BT0001 handling "legacy" bigtable column. You might also need to clear all your cache/cookies if you encounter
"This webpage is not available
The connection to [appId].appspot.com was interrupted.".
Unfortunately, there is no known way to test this type of error locally/dev_server.

. If you install third party Tapestry component, do not forget to include its corresponding .tml file, otherwise you will see error like

Caused by: org.apache.tapestry5.ioc.util.UnknownValueException: Unable to resolve 'jquery/autocomplete' to a component class name.

. If you uses Wicket (deploy_wicket), you need to remove wicket-jmx jar in your app, it will fail with an error: 
java.lang.NoClassDefFoundError: java.lang.management.ManagementFactory is a restricted class.

. If you add a new wicket page, other than creating the .html and .java, please do not forget to mount the page with mountBookmarkablePage in WicketApplication.java.
Also if the JPA's exclude-unlisted-classes is set to false in persistence.xml, do not forget to add the entity class too in persistence.xml.

. If you use Struts 2, you probably need to change guest.properties (for form) and package.properties/[actionClass]-[actionName]-validation.xml (for validations).

. If you encounter NoSuchMethodException exception like
"java.lang.NoSuchMethodException: com.aurifa.struts2.tutorial.action.PartnerAction.save()
	at java.lang.Class.getMethod(Class.java:1605)"
	Please make sure that your persistence Id is of the hidden type, in the above, i.e.
<s:hidden name="partner.id" value="%{partner.id}"/>
or
In your web.xml ensure you have a filter-mapping defined for the /struts/...<js files>.  You need to make sure that a request to the Javascript and CSS files defined in the struts jar file go thru the Struts 2 filter.  That is, make sure you have something like this on your web.xml: 

    <filter-mapping>  
        <filter-name>struts2Filter</filter-name>  
        <url-pattern>/struts/*</url-pattern>  
    </filter-mapping>

. If you have issue with legacy big data that does not exist previous and you already used an object instead of the primitive type,
use try-catch to address it
e.g.
	try {
		if(u.getUseDescription()) {
			useDescription = true;
		}
	} catch (Exception e) {
		System.out.println("legacy entity without useDescription flag: " + u);
	}
*** If you have a better idea how to address this, please email me. Thanks!
				
. CKEditor
If you need to customize basic or full toolbar, the file that you need to change is 
src/com/plannow/tapestry5/ckeditor/mixins/ckeditor/config.js.

. Spring MVC
For signup service (not implemented, yet), please consider developing it with UsernamePasswordAuthenticationToken and use the custom object, GaeUserDetails.


Known Issues -

. Since 1.7.2, HttpServletResponse.isCommited() returns false after HttpServletResponse.sendRedirect
Since 1.7.1+ Tapestry apps no longer work in the dev app server.
https://code.google.com/p/googleappengine/issues/detail?id=8201

. Cascade does not apply to DELETE
http://stackoverflow.com/questions/2995693/google-app-engine-delete-jpql-query-and-cascading

. Cascade is from parent deletion down to child, not the association
http://stackoverflow.com/questions/2011519/jpa-onetomany-not-deleting-child

. java.lang.OutOfMemoryError: PermGen space
Change the GAEJ WebApplication "Run Configuration"
-Xmx612m to -XX:PermSize=256M -XX:MaxPermSize=512M -Xmx1024M
Do not forget to restart the Eclipse IDE once this is set, otherwise it would not work!

. "CreateProcess error=87, The parameter is incorrect" issue
GPE seems to have issue with the long classpath, this seems to be Windows (yes, including up to Windows 8 Developer's Preview) only issue.
Hopefully workaround of adding ONLY the package name(s) that you need data enhancement helps i.e.
try setting the ORM under project properties -> google -> app engine -> ORM  to just the related folder(s).
http://stackoverflow.com/questions/3992136/datanucleus-enhancer-javaw-the-parameter-is-incorrect
http://code.google.com/p/google-web-toolkit/issues/detail?id=4395#c15

. If you deployed your app on GAEJ, and click save and then another on quickly, you will see duplicated entry with different ID.
There will be no exception thrown.

. SVN Codesion standalone test (non servlet) gave the following error -
Caused by: java.lang.NullPointerException: No API environment is registered for this thread. 
Please refer to http://code.google.com/appengine/docs/java/tools/localunittesting.html.
. Startup time with Wicket 1.4.7 is like 18 s but without it, the startup is just 8 s (JSP+Tapestry).
. Compass issues:
a. Indexing would not work (would not return the results) if you are not using JDO.
b. Any term that is immediately followed by ":" will cause the following unknown exception -
org.compass.core.engine.SearchEngineQueryParseException: Failed to parse query [template:]; nested exception is org.apache.lucene.queryParser.ParseException: Cannot parse 'template:': Encountered "<EOF>" at line 1, column 9.
Was expecting one of:
    "(" ...
    "*" ...
    <QUOTED> ...
    <TERM> ...
    <PREFIXTERM> ...
    <WILDTERM> ...
    "[" ...
    "{" ...
    <NUMBER> ...
See http://lucene.apache.org/java/2_4_1/queryparsersyntax.html for details usage.

. Compass 2.4.1 does not work with Lucene 2.9.4 or above, gave this error -
Error for /task/compassindex
java.lang.NoSuchMethodError: org.apache.lucene.index.LogByteSizeMergePolicy: method <init>()V not found
	at org.compass.core.lucene.engine.merge.policy.LogByteSizeMergePolicyProvider.create(LogByteSizeMergePolicyProvider.java:34)

. Struts 2 Issue
Caused by: com.opensymphony.xwork2.inject.ContainerImpl$MissingDependencyException: No mapping found for dependency [type=com.opensymphony.xwork2.ObjectFactory, name='default'] in public void com.opensymphony.xwork2.config.providers.XmlConfigurationProvider.setObjectFactory(com.opensymphony.xwork2.ObjectFactory).
Workaround is to place the following line in the struts.xml -
	<include file="struts-default.xml"/>
Thanks to http://x.iny.a.blog.163.com/blog/static/127025732201071552151737/

java.lang.IllegalArgumentException: Comparison method violates its general contract!
	at java.util.ComparableTimSort.mergeHi(ComparableTimSort.java:835)

This error happened only on the production (as SDK 1.5.4), and there is no known workaround at the time this is written (10/1/2011).
Thus the Struts 2.2.3.1 changes have been stored into branch and not part of the main trunk.
http://code.google.com/p/googleappengine/issues/detail?id=5890

For some reason, action with direct method invocation does not work e.g.
		<action name="crud" class="com.aurifa.struts2.tutorial.action.PartnerAction" method="input">
			<result name="success" type="redirectAction">partnerindex</result>
			<result name="input">/gu1/partnerForm.jsp</result>
			<result name="error">/gu/error.jsp</result>
		</action>
and
<s:url id="url" action="crud!input" /> or 
<s:url id="url" action="crud" method="input" />

Please define a separate action instead, e.g.
		<action name="crudInput">
			<result>/gu1/partnerForm.jsp</result>
		</action>		
and
<s:url id="url" action="crudInput" />
http://stackoverflow.com/questions/1777714/struts2-action-not-calling-properly

Themes Issue -
http://freemarker.blogspot.com/2010/02/freemarker-on-google-app-engine.html
Freemarker jar file removed (6/15/2013)


java.lang.ClassNotFoundException: org.apache.xerces.jaxp.SAXParserFactoryImpl issue -
It seems like the new selenium standalone server (2.12.0) is having sax parser with some components. Thus until there is a fix, the workaround is 
to keep the old copy  (2.0a6) in the war/WEB-INF/lib folder and use that for compilation only (not for running the server).
This error affects Tapestry 5.3 too.

About running Cloud SQL based Google App Engine for Java

1. Make sure you do not use hibernate with JPA 2.0 (e.g. hibernate-jpa-2.0-api-1.0.0.Final.jar) as GAEJ supports only JPA 1.0.
2. Make sure you have the correct version of hibernate. A good one that works is like hibernate-core-3.3.2.GA.jar.
3. Make sure your settings are correct, for Struts 2 e.g.
    <!-- Struts theme -->
	<constant name="struts.ui.templateDir" value="template" />
    <constant name="struts.ui.theme" value="xhtml" />
    <!-- Development mode -->
    <constant name="struts.devMode" value="false" />
    <!-- Enable global file 'global.properties' -->
    <constant name="struts.custom.i18n.resources" value="global" />
4. Make sure you use the right version of MySQL and have the right settings in hibernate.cfg.xml e.g.
    <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
    <property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
5. Make sure you have the following filter mapping for /struts-tags to work properly (i.e. <%@taglib uri="/struts-tags" prefix="s"%>):
	<filter-mapping>  
        <filter-name>struts2</filter-name>
        <url-pattern>/struts/*</url-pattern>  
    </filter-mapping>
and also any application specific uri e.g.
	<filter-mapping>
		<filter-name>struts2</filter-name>
		<url-pattern>/gu/*</url-pattern>
	</filter-mapping>
6. If you are using Struts, remember to append the .action (if you do not change the default) e.g.
http://localhost:8888/movie/index.action

strutstool setup -

1. Comment out the struts adapter in displaytag.properties, e.g.
#locale.resolver=org.displaytag.localization.I18nStrutsAdapter
#locale.provider=org.displaytag.localization.I18nStrutsAdapter (there is an extra space here)

2. Do not forget to set the ognl securitymanager to null in web.xml, good reference is at
http://programmingpanda.blogspot.com/2009/07/struts-2-ongl-issue-on-google-app.html. 
Or use com.ociweb.gaestruts2.InitListener, which is already written for you :)

3. Do not forget to map the struts resources as static resources in appengine.xml i.e.
	<sessions-enabled>true</sessions-enabled>
	<static-files> 
	    <include path="/struts/*" /> 
	    <include path="/js/*" /> 
	</static-files> 
4. Do not forget about the struts 2 mapping e.g.
	<filter-mapping>  
        <filter-name>struts2</filter-name>
        <url-pattern>/struts/*</url-pattern>  
    </filter-mapping>
	<filter-mapping>  
        <filter-name>struts2</filter-name>
        <url-pattern>/movie/*</url-pattern>  
    </filter-mapping>
on top of
    <filter-mapping>
        <filter-name>struts2</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

5. Your POJO's id should be of type Long

6. You need to uncomment the following
    <property name="hibernate.hbm2ddl.auto">update</property>
<!--    
-->

7. Comment out the following in hibernate.cfg.xml
    <!-- Hibernate Search Configuration -->
<!--    
    <property name="hibernate.search.default.directory_provider">filesystem</property>
    <property name="hibernate.search.default.indexBase">/home/maycon/lucene/indexes</property>
-->
and use
    <property name="hibernate.search.default.directory_provider">org.hibernate.search.store.impl.RAMDirectoryProvider</property>
    <property name="hibernate.search.default.indexBase">/indexes</property>
please remember to create directory /indexes under war/

8. Comment out wro to avoid jmx error
    <!-- Start: wro4j configuration -->
<!--
    <filter>
        <filter-name>WebResourceOptimizer</filter-name>
        <filter-class>ro.isdc.wro.http.WroFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>WebResourceOptimizer</filter-name>
        <url-pattern>/resources/*</url-pattern>
    </filter-mapping>
-->

9. Modify org.jboss.logging.LoggerProviders so that it does not refer to java.util.logging.LogManager.
Already done if you use this project :)

10. Modify com.opensymphony.xwork2.util.LocalizedTextUtil so that it does not use any concurrent collection classes.
Already done if you use this project :)

11. Modify com.framework.util.browser.BrowserSniffer in such a way -
//import org.apache.commons.lang.xwork.NullArgumentException;
//import org.apache.commons.lang.xwork.StringUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

12. Unfortunately Hibernate validation based on JSR 303 is not working on Google App Engine, so you can not use that.
Otherwise you will encounter "org.hibernate.cfg.beanvalidation.TypeSafeActivator with modifiers "public static" if you are lucky, 
worst you will see "java.lang.NoClassDefFoundError: Could not initialize class com.framework.util.hibernate.HibernateUtil" error.
For example:

//    @NotNull
//    @XSSFilter
and set
    <property name="javax.persistence.validation.mode">none</property>
in hibernate.cfg.xml

13. Make sure the following flags are off in struts.xml -
<constant name="struts.devMode" value="false" />
<constant name="struts.i18n.reload" value="false" />
<constant name="struts.configuration.xml.reload" value="false" />

14. Change
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
to
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
in hibernate.cfg.xml

15. Modify TextBlock according to remove dependency with javax.swing.tree.TreeNode e.g. discussed at https://groups.google.com/forum/#!topic/google-appengine-java/3YTkT2BEmMQ.
Already done if you use this project :)

16. If you need file upload, you might need to refer to http://code.google.com/p/struts2-gae/ as well.
However, it is not necessary to make strutstool works if you do not need file upload.

17. apple-touch-icon.png is just for iOS webview (which includes Android Chrome!)
http://stackoverflow.com/questions/14986650/keep-getting-404s-for-apple-touch-icon-png

Spring Optimization -

Please review https://cloud.google.com/appengine/articles/spring_optimization, it's good to know that <constructor-arg name is discouraged etc.

Also for component scan i.e. context:component-scan is not used, besides the tag, check for and avoid @ComponentScan annotation too if possible.


Compass Issue -

@SearchableProperty annotation
It seems like if you have previously annotated a property to be searchable with this @SearchableProperty, 
and later decide to remove it (make it non-searchable), you will encounter the exception -

java.lang.IllegalArgumentException: Failed to find mapping for alias [alias_name] and path [alias_name.property_name]

during runtime. I have not get a chance to figure out how to resolve that and write it down to share with all. If you know how to fix
it, please kindly email me :)


GAEJ Issues [applicable SDK version] -

. [1.6.4] 
If your web.xml is invalid, like having duplicated filter mappings e.g.

<!-- enable appstats (http://code.google.com/appengine/docs/java/tools/appstats.html) -->
	<servlet>
        <servlet-name>appstats</servlet-name>
        <servlet-class>com.google.appengine.tools.appstats.AppstatsServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>appstats</servlet-name>
        <url-pattern>/appstats/*</url-pattern>
    </servlet-mapping>
   	<filter>
        <filter-name>appstats</filter-name>
        <filter-class>com.google.appengine.tools.appstats.AppstatsFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>appstats</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
	<filter>
        <filter-name>appstats</filter-name>
        <filter-class>com.google.appengine.tools.appstats.AppstatsFilter</filter-class>
        <init-param>
            <param-name>logMessage</param-name>
            <param-value>Appstats available: /appstats/details?time={ID}</param-value>
        </init-param>
    </filter>    

This will not be caught by the SDK during deployment and render your deployment hanging at
"Successfully processed" stage e.g.
INFO: Successfully processed /var/folders/l7/4b7js00n4z32gr5z9cr1mj4c0000gn/T/appcfg1225440484603113498.tmp/WEB-INF/web.xml

The mode of the file should be rw-r--r--.

So becareful what you add/change in web.xml like appstats configuration!

Another possibility is, One can not have a file with 3 dots under WEB-INF e.g. web.xml.core.appstats.

There is also a reported case where in web.xml mapping for security-constraint with /** which cause the hanging.
It was wrote that changing it to /* resolved the issue.
http://stackoverflow.com/questions/10113477/google-app-engine-spinning-while-scanning-for-jsps-during-deploy

. Enabling custom error, cause the following exception -

com.google.apphosting.utils.config.AppEngineConfigException: No static file found for error handler: /no_more_quota.html, out of [__static__/static/wait.html, __static__/error/no_more_quota.html, __static__/static/index.html, __static__/static/employee.html]

. [1.6.5] 
This application does not work with this version of SDK. The error was DeadlineExceededException as well as HardDeadlineException.
No explaination for now.

. [1.6.6]
Application seems to work with this version. Howver, under certain condition like Lucene is performing the indexing, the following new exception is noticed -
Caused by: java.lang.NullPointerException
	at com.google.appengine.api.utils.FutureWrapper.get(FutureWrapper.java:90)
	at com.google.appengine.api.datastore.FutureHelper$CumulativeAggregateFuture.get(FutureHelper.java:145)
	at com.google.appengine.api.utils.FutureWrapper.get(FutureWrapper.java:90)
	at com.google.appengine.api.datastore.FutureHelper.getInternal(FutureHelper.java:72)
	at com.google.appengine.api.datastore.FutureHelper.quietGet(FutureHelper.java:33)
	
Some suggestions to avoid this error includes -
1. Flushing the memcache
2. Setting 	
	<threadsafe>false</threadsafe>

https://groups.google.com/forum/?fromgroups#!topic/google-appengine/zvkaIE4_MPU
http://stackoverflow.com/questions/10675996/temporary-error-retrieving-documents-in-full-text-search
http://stackoverflow.com/questions/10612108/datastore-throwing-exceptions-when-getting-entities-by-key
http://stackoverflow.com/questions/10461251/jdofataluserexception-illegal-argument-debug-as-google-web-application

. [1.7.5]
http://tapestry.1045711.n5.nabble.com/java-lang-IllegalStateException-Sanity-check-neither-a-stream-response-nor-a-redirect-response-was-g-td5717915.html
Error:

"An unexpected application exception has occurred.

java.lang.IllegalStateException
Sanity check - neither a stream response nor a redirect response was generated for this action request."

There is no fix provided by Google currently, but it seems to happen only on local DEV server.
The related ticket is at https://code.google.com/p/googleappengine/issues/detail?id=8201. Star it if and/or ask Google to fix it if you could!

However, there is this workaround at https://bitbucket.org/akochnev/tap5-gae-utils/wiki/Home by a kind soul :)

. [1.8.0]
precompile extremely slow

https://code.google.com/p/googleappengine/issues/detail?id=9377

. [1.8.2]
java.io.InvalidClassException: com.google.appengine.repackaged.com.google.io.protocol.ProtocolMessage; local class incompatible: stream classdesc serialVersionUID = 5795572945429871714, local class serialVersionUID = -2040267499882372295

WARNING: Error starting handlers
javax.xml.parsers.FactoryConfigurationError: Provider org.apache.xerces.jaxp.SAXParserFactoryImpl not found

http://stackoverflow.com/questions/1016286/org-apache-xerces-jaxp-saxparserfactoryimpl-not-found-when-importing-gears-api-i

Workaroud:
Importing the jar to war/WEB-INF/lib
Adding the jar to the build path

. [1.8.3-]
WARNING: java.lang.IllegalArgumentException: Cookie name "04 Apr 2013 23:05:50" is a reserved token
-Not sure what is this, but it does not seem to affect anything.

java.lang.AbstractMethodError: javax.servlet.jsp.JspFactory.getJspApplicationContext(Ljavax/servlet/ServletContext;)Ljavax/servlet/jsp/JspApplicationContext;
-This is not an issue with the app, it is an occasional "hiccups" of Google App Engine for Java!
-Workaround: just hit refresh or retry again after couples of minutes.

. [1.8.4]
java.lang.RuntimeException: Unable to locate the App Engine agent. Please use dev_appserver, KickStart, or set the jvm flag: "-javaagent:<sdk_root>/lib/agent/appengine-agent.jar"

Click-right on project > Properties > Java Compiler > Compiler Compliance Levels: make sure 1.6 is selected. Even if you've upgraded to 1.7, it's useless.
If it does not work, set it to 1.7, click Apply, let it build and run it (with error of course) and then set it back to 1.6. Now it should work!

http://irina-magdici.blogspot.com/2011/12/import-gwt-google-app-engine-project-to.html

java.lang.AbstractMethodError: javax.servlet.jsp.JspFactory.getJspApplicationContext(Ljavax/servlet/ServletContext;)Ljavax/servlet/jsp/JspApplicationContext;
-This turned out to be that I have for some reason copy selenium-server-standalone-2.35.0.jar into WEB-INF/lib and SDK 1.8.4 does not like that!
-Remove the selenium-server-standalone-2.35.0.jar from the WEB-INF/lib will resolve the exception (which occured only in production server, yes, on
dev_server, it was fine)

But this breaks the development server with error:

WARNING: failed JettyContainerService$ApiProxyHandler@5b5b55bc: javax.xml.parsers.FactoryConfigurationError: Provider org.apache.xerces.jaxp.SAXParserFactoryImpl not found
Sep 18, 2013 6:02:06 PM com.google.apphosting.utils.jetty.JettyLogger warn
WARNING: Error starting handlers
javax.xml.parsers.FactoryConfigurationError: Provider org.apache.xerces.jaxp.SAXParserFactoryImpl not found

So the workaround is to copy the jar back to WEB-INF/lib if deployed locally and if deployed on the cloud, remove it!
 
. [1.8.6]

This release does not seem to work with Mac OS X 10.8 and 10.9 with Eclipse GPE 4.3.
There is no known workaround at this point, so please stick with 1.8.4 for now.

. [1.8.7]

Remove copying of selenium-server-standalone-2.35.0.jar into WEB-INF/lib in deploy*.sh/.cmd. Issue which was occuring
 in 1.8.4 seems to be fixed.
 
. [1.8.8]
 
Redirect loop issue. Adding 'requires-channel="https"' in Spring Security 3.1.4 intercept-url will cause infinite redirect loop e.g.

 		<intercept-url pattern="/sr" access="hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')" requires-channel="https"/>
 
Logout action of Tapestry 5.3.X will cause "java.lang.IllegalStateException: Sanity check - neither a stream response 
nor a redirect response was generated for this action request." exception with Spring Security 3.1.4 (only for 
local DEV server).

. [1.8.9]

This release break the app as there seems to be a conflict between asm 4 with asm 3 but it is not obvious where is the asm 3 as that has been
removed sometime back.

. [1.9.0] - First Official Stable Release!!!

The asm conflicts is fixed but deployment via GPE gave the following error:

Preparing to deploy:
	Created staging directory at: 'C:\Users\Monroe\AppData\Local\Temp\appcfg1815451310892456753.tmp'
	Scanning for jsp files.
	Compiling jsp files.
java.lang.RuntimeException: Cannot get the System Java Compiler. Please use a JDK, not a JRE.

Just use the bin/ command for now.

. [1.9.2]

Comments issue with CapeDwarf:

	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>
			/WEB-INF/spring/springsecurity-context.xml
<!--
			/WEB-INF/spring/application-context.xml
			/WEB-INF/spring/gaej-beans.xml
-->
<!--
			/WEB-INF/spring/beans.xml
-->
 		</param-value>
		<!-- https://bitbucket.org/akochnev/tap5-gae-utils/wiki/Home -->
		<param-name>tapestry.DevelopmentMode-modules</param-name>
        <param-value>com.troymaxventures.tapestry.gaeutils.GaeDevServerModule</param-value>
	</context-param>

https://community.jboss.org/thread/240198?start=15&tstart=0

. [1.9.3]

Does not seems to enhance the 20 classes that needs to be enhanced on Windows 7 platform.

. [1.9.12]

The GAEJ does not work with Spring 4.1.0 framework which is based on Servlet 3.0 API, with the following error:

java.lang.NoSuchMethodError: javax.servlet.http.HttpServletResponse.getStatus()I
	at org.springframework.web.servlet.FrameworkServlet.publishRequestHandledEvent(FrameworkServlet.java:1066)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:996)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:852)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:617)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:837)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:717)
	at org.mortbay.jetty.servlet.ServletHolder.handle(ServletHolder.java:511)

"In order to remain compatible with Google App Engine and older application servers, it is possible to deploy a Spring 4 application into a Servlet 2.5 environment."

http://docs.spring.io/spring/docs/current/spring-framework-reference/html/new-in-4.0.html


Subversion with Subclipse

You need at least 1.10.1 of Subversion client adapter, get it from http://subclipse.tigris.org/update_1.10.x/.

10/3/2018

Good Reference -

http://groups.google.com/group/google-appengine-java/web/will-it-play-in-app-engine
http://code.google.com/p/kaptcha/
http://www.javacodegeeks.com/2013/01/spring-data-jpa-and-pagination.html
https://cloud.google.com/appengine/docs/standard/java/datastore/jpa/overview
https://cloud.google.com/appengine/docs/standard/java/runtime-java8
https://medium.com/google-cloud/beyond-java-8-on-google-app-engine-standard-environment-bc97cc67d026
