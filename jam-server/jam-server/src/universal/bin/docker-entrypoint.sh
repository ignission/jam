#!/bin/sh

if [ ! -z "${JAVA_OPTIONS}" ]; then
    echo "Using java options: ${JAVA_OPTIONS}"
fi

if [ -z "${OPENVIDU_SERVER_URL}" ]; then
    echo "OPENVIDU_SERVER_URL is required"
    exit 1
fi

until curl ${OPENVIDU_SERVER_URL} --insecure > /dev/null 2>&1; do
    echo "**** OpenVidu server - waiting"
    sleep 1
done

/opt/docker/bin/jam-http-server
