set T_VERSION=5.3.8

REM goto http://tapestry.apache.org/download.html, download all jar files and copy them into 1/

cd %USERPROFILE%\Downloads\apache-tapestry-5.3.8-bin

copy tapestry-core-%T_VERSION%.jar c:\GoogleAppEngine\war\WEB-INF\lib
copy tapestry-func-%T_VERSION%.jar c:\GoogleAppEngine\war\WEB-INF\lib
copy tapestry-ioc-%T_VERSION%.jar c:\GoogleAppEngine\war\WEB-INF\lib
copy tapestry-json-%T_VERSION%.jar c:\GoogleAppEngine\war\WEB-INF\lib
copy tapestry-test-%T_VERSION%.jar c:\GoogleAppEngine\war\WEB-INF\lib
copy tapestry-yuicompressor-%T_VERSION%.jar c:\GoogleAppEngine\war\WEB-INF\lib
copy tapestry5-annotations-%T_VERSION%.jar c:\GoogleAppEngine\war\WEB-INF\lib
copy plastic-%T_VERSION%.jar c:\GoogleAppEngine\war\WEB-INF\lib

pause	