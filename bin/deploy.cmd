call setEnv.cmd

echo on
mkdir c:\temp
set TEMP=c:\temp
set TMP=c:\temp

cd %~dp0
cd ..\..
set PROJECT_HOME=%CD%


echo %CLASSPATH%

start appcfg --enable_jar_splitting update "%PROJECT_HOME%\GoogleAppEngine\war"
:start appcfg --no_cookies --enable_jar_splitting update "%PROJECT_HOME%\GoogleAppEngine\war"
cd %~dp0

:pause