call setenv.bat

call sethome.bat

:netstat -ano | find "8080"
:taskkill /F /PID 10340

%GAE_JAVA_SDK_HOME%\bin\dev_appserver.cmd %PROJECT_HOME%\war & start http://localhost:8080/

:%JAVA_HOME%\bin\java %VERBOSE% -Xmx1048m -javaagent:%GAE_JAVA_SDK_HOME%/lib/agent/appengine-agent.jar -classpath %GAE_JAVA_SDK_HOME%/lib/appengine-tools-api.jar  com.google.appengine.tools.development.DevAppServerMain --port=8888 --address=0.0.0.0 ../war