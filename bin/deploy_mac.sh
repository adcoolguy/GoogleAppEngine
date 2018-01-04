#! /usr/bin/env bash

#Need to create soft link to your GAEJ home e.g.
#ln -s /Applications/appengine-java-sdk-1.9.32 /appengine-java-sdk
#sudo ln -s /Applications/appengine-java-sdk-1.8.8 /appengine-java-sdk
#ln -s '/Users/macbook/GoogleAppEngine' /project_home
#sudo chmod 755 /appengine-java-sdk/bin/appcfg.sh

#Also need to make sure Java is installed properly, generally available at /usr/bin/java and 
#change JAVA_HOME accordingly
#export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home
#export JAVA_HOME='/System/Library/Frameworks/JavaVM.framework/Home'
export GAE_JAVA_SDK_HOME='/appengine-java-sdk'
#export PATH=${PATH}:$JAVA_HOME/bin:$GAE_JAVA_SDK_HOME/bin
PROJECT_HOME='/project_home'
export JAVA_OPTS='-XX:MaxPermSize=1024m'
#Reference: https://developers.google.com/appengine/docs/java/tools/uploadinganapp
#$GAE_JAVA_SDK_HOME/bin/appcfg.sh ${JAVA_OPTS} --enable_jar_splitting update ${PROJECT_HOME}/GoogleAppEngine/war
echo $GAE_JAVA_SDK_HOME/bin/appcfg.sh --oauth2 --use_java7 --enable_jar_splitting update ${PROJECT_HOME}/war
#rm ~/.appcfg_cookies
#$GAE_JAVA_SDK_HOME/bin/appcfg.sh --no_cookies --use_java7 --enable_jar_splitting update ${PROJECT_HOME}/war
$GAE_JAVA_SDK_HOME/bin/appcfg.sh --enable_jar_splitting update ${PROJECT_HOME}/war
