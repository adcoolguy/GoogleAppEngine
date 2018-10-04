call setenv.bat

set GAE_JAVA_SDK_HOME=C:\appengine-java-sdk-1.9.65
:set TCLASSPATH=..\war\WEB-INF\classes_
set TCLASSPATH=..\war\WEB-INF\classes

REM https://cloud.google.com/appengine/docs/standard/java/datastore/jdo/overview

REM WARNING: Requires Java 8 runtime!!!

%JAVA_HOME%\bin\java -cp %TCLASSPATH%;%GAE_JAVA_SDK_HOME%\lib\appengine-tools-api.jar;%GAE_JAVA_SDK_HOME%\lib\user\appengine-api-1.0-sdk-1.9.65.jar;%TCLASSPATH%;%PROJECT_HOME%\mygae.jar com.google.appengine.tools.enhancer.Enhance %TCLASSPATH%\app\model\*.class %TCLASSPATH%\tapp\model\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\common\model\*.class %TCLASSPATH%\com\appspot\cloudserviceapi\security\spring\model\GaeDaoAuthenticationProvider.class

rem 
