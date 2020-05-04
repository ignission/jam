FROM ubuntu:16.04

MAINTAINER shoma416@gmail.com

# Install Java, supervisor and netstat
RUN set -xe \
    && apt-get update \
    && apt-get install -y \
    curl \
    wget \
    openjdk-8-jre \
    && rm -rf /var/lib/apt/lists/*

COPY dist/emoi-server.jar /

COPY ./docker-entrypoint.sh /docker-entrypoint.sh

RUN chmod +x /docker-entrypoint.sh

EXPOSE 8855

CMD /docker-entrypoint.sh