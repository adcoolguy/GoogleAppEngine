call setenv.bat

call sethome.bat

REM Java classpath has to be an absolute path!
:set TCLASSPATH=%userprofile%\gaej\war\WEB-INF\classes_
set TCLASSPATH=%userprofile%\gaej\war\WEB-INF\classes
set SDK_JAR=%GAE_JAVA_SDK_HOME%\lib\user\appengine-api-1.0-sdk-1.9.65.jar
set TSRC=%userprofile%\gaej\src

call setclasspath.bat

REM https://cloud.google.com/appengine/docs/standard/java/datastore/jdo/overview

REM WARNING: Requires Java 8 runtime!!!
REM Open the following to see if you can compile a Java class
:%JAVA_HOME%\bin\javac -cp %GAE_JAVA_SDK_HOME%\lib\appengine-tools-api.jar;%SDK_JAR%;%APP_LIB%;%TSRC% %TSRC%\com\appspot\cloudserviceapi\security\spring\model\GaeUserDetails.java

%JAVA_HOME%\bin\java -cp %GAE_JAVA_SDK_HOME%\lib\appengine-tools-api.jar;%SDK_JAR%;%APP_LIB%;%TCLASSPATH% com.google.appengine.tools.enhancer.Enhance %TCLASSPATH%\app\model\*.class %TCLASSPATH%\tapp\model\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\common\model\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\security\spring\model\*.class 

rem  %TCLASSPATH%\com\appspot\cloudserviceapi\security\spring\model\GaeDaoAuthenticationProvider.class 

REM http://techxperiment.blogspot.com/2017/07/dealing-with-exceptions-occured-while.html
:%JAVA_HOME%\bin\java -cp .\enhancer\*;%SDK_JAR%;%APP_LIB%;%TCLASSPATH% org.datanucleus.enhancer.DataNucleusEnhancer %TCLASSPATH%\app\model\*.class %TCLASSPATH%\tapp\model\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\common\model\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\security\spring\model\*.class 

rem %TCLASSPATH%\com\appspot\cloudserviceapi\security\spring\model\GaeGrantedAuthority.class
