:cd %~dp0
:cd ..\..
set PROJECT_HOME=..

set JAVA_HOME=\jdk1.7.0_80
rem "Please make sure your JDK is set correctly as above, i.e. in the root directory! Otherwise, JSP compilation will fail."
rem set GAE_JAVA_SDK_HOME=\appengine-java-sdk-1.9.65
set M2_HOME=\apache-maven-3.3.9
set ROO_HOME=\spring-roo-1.1.0.M3
set ANT_HOME=\apache-ant-1.6.5

set PATH=%JAVA_HOME%\bin;%GAE_JAVA_SDK_HOME%\bin;%MAVEN_HOME%\bin;%ROO_HOME%\bin;%ANT_HOME%\bin;%PATH%

cd %~dp0
