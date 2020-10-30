#!/bin/bash

if [ ! -z "${JAVA_OPTIONS}" ]; then
    echo "Using java options: ${JAVA_OPTIONS}"
fi

if [ -z "${OPENVIDU_SERVER_URL}" ]; then
    echo "OPENVIDU_SERVER_URL is required"
    exit 1
fi

until curl ${OPENVIDU_SERVER_URL} --insecure > /dev/null >&2; do
    echo "**** OpenVidu server - waiting"
    sleep 1
done

until echo '\q' | mysql -h"${DATABASE_HOST}" -P "${DATABASE_PORT}" -u"${DATABASE_USER}" -p"${DATABASE_PASS}" "${DATABASE_NAME}" ; do >&2 
    echo "**** MySQL is unavailable - sleeping"
    sleep 1
done

/opt/docker/bin/jam-http-server
