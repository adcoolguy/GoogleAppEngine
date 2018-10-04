call setEnv.cmd

cd %~dp0
cd ..\..
set PROJECT_HOME=%CD%

start appcfg --enable_jar_splitting rollback "%PROJECT_HOME%\gaej\war"

:exit
:pause