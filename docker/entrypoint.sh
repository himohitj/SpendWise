#!/bin/sh
set -eu

# Render assigns PORT at runtime. Tomcat normally listens on its fixed 8080 port.
render_port="${PORT:-8080}"
sed -i "s/port=\"8080\"/port=\"${render_port}\"/" /usr/local/tomcat/conf/server.xml

exec catalina.sh run
