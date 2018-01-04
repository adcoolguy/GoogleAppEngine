set GAEJ_VERSION=1.9.60

mkdir %userprofile%\tmp && cd %userprofile% && curl --insecure https://storage.googleapis.com/appengine-sdks/featured/appengine-java-sdk-1.9.59.zip > %userprofile%\tmp\t.zip && unzip %userprofile%\tmp\t.zip && move appengine-java-sdk-1.9.59