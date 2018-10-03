call setenv.bat

echo on
mkdir c:\temp
set TEMP=c:\temp
set TMP=c:\temp

cd %~dp0
cd ..\..
set PROJECT_HOME=%CD%


echo %CLASSPATH%

appcfg --enable_jar_splitting update "%PROJECT_HOME%\gaej\war"
:appcfg --no_cookies --enable_jar_splitting update "%PROJECT_HOME%\gaej\war"
cd %~dp0

:pause