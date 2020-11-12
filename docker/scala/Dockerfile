FROM alpine:3.12

ARG SBT_VERSION=1.4.2

RUN set -xe \
  && apk update \
  && apk add --no-cache openjdk11-jre-headless curl bash sudo \ 
  && curl -L -o sbt-${SBT_VERSION}.tgz https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz \
  && tar -xvzf sbt-${SBT_VERSION}.tgz \
  && rm sbt-${SBT_VERSION}.tgz

ENV PATH $PATH:/sbt/bin

RUN set -xe \
  && addgroup -g 1000 user \
  && adduser -S user --uid 1000 -G user \
  && echo "user ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers \
  && echo 'user:user' | chpasswd 

COPY docker-entrypoint.sh /docker-entrypoint.sh

USER user

RUN sbt -V

WORKDIR /work

ENTRYPOINT ["/docker-entrypoint.sh"]
