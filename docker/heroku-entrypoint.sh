#!/bin/sh
set -eu

# Heroku assigns the web port through PORT. Tomcat 10.1 reads its connector
# port from server.xml, so replace the image default before starting Tomcat.
app_port="${PORT:-8080}"
sed "s/port=\"8080\"/port=\"${app_port}\"/" /usr/local/tomcat/conf/server.xml > /tmp/server.xml
cat /tmp/server.xml > /usr/local/tomcat/conf/server.xml

exec catalina.sh run
