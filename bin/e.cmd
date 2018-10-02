call setenv.bat

set GAE_JAVA_SDK_HOME=C:\appengine-java-sdk-1.9.65
set TCLASSPATH=..\war\WEB-INF\classes_

REM https://cloud.google.com/appengine/docs/standard/java/datastore/jdo/overview

java -cp %GAE_JAVA_SDK_HOME%\lib\appengine-tools-api.jar;%TCLASSPATH%;%PROJECT_HOME%\mygae.jar com.google.appengine.tools.enhancer.Enhance %TCLASSPATH%\tapp\model\*.class 

rem %TCLASSPATH%\com\appspot\cloudserviceapi\common\*.class
