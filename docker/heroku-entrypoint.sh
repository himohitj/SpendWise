#!/bin/sh
set -eu

# Heroku exposes the assigned HTTP port through PORT.
# Configure Tomcat's HTTP connector to bind to that port.
render_port="${PORT:-8080}"
sed -i "s/port=\"8080\"/port=\"${render_port}\"/" /usr/local/tomcat/conf/server.xml

exec catalina.sh run
