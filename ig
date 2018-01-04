export GAEJ_VERSION=1.9.60

mkdir ~/tmp || true && cd ~/tmp

rm -rf appengine-java-sdk-"$GAEJ_VERSION"

export tcommand="wget https://storage.googleapis.com/appengine-sdks/featured/appengine-java-sdk-"$GAEJ_VERSION".zip"

#echo 
$tcommand

unzip appengine-java-sdk-"$GAEJ_VERSION".zip && mv appengine-java-sdk-"$GAEJ_VERSION"/ ~