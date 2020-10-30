#!/bin/sh

if [ ! -z "${JAVA_OPTIONS}" ]; then
    echo "Using java options: ${JAVA_OPTIONS}"
fi

/opt/docker/bin/jam-websocket-server
