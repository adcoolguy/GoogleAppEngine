#! /usr/bin/env bash

#if you get "You do not have permission to modify this app (app_id=u'...", execute the following:
#rm ~/.appcfg_oauth2_tokens_java

#Need to create soft link to your GAEJ home e.g.
#sudo ln -s ~/appengine-java-sdk-1.9.60 /appengine-java-sdk
#ln -s '/Users/ag' /project_home

#Also need to make sure Java is installed properly, generally available at /usr/bin/java and 
#change JAVA_HOME accordingly

export JAVA_HOME='/System/Library/Frameworks/JavaVM.framework/Home'
export GAE_JAVA_SDK_HOME='/appengine-java-sdk'
#export PATH=${PATH}:$JAVA_HOME/bin:$GAE_JAVA_SDK_HOME/bin
PROJECT_HOME='/project_home'

echo 'copying ' ${PROJECT_HOME}/GoogleAppEngine/war/WEB-INF/web.xml.all ' to ' ${PROJECT_HOME}/GoogleAppEngine/war/WEB-INF/web.xml ' ...'
cp -f ${PROJECT_HOME}/GoogleAppEngine/war/WEB-INF/web.xml.all ${PROJECT_HOME}/GoogleAppEngine/war/WEB-INF/web.xml
rm current.web.*
touch current.web.all
ls current.web.*
#./deploy_mac.sh