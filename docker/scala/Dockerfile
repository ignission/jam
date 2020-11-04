FROM alpine:3.12

ARG SBT_VERSION=1.4.2

RUN set -xe \
  && apk update \
  && apk add --no-cache openjdk11-jre-headless curl bash \ 
  && curl -L -o sbt-${SBT_VERSION}.tgz https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz \
  && tar -xvzf sbt-${SBT_VERSION}.tgz \
  && rm sbt-${SBT_VERSION}.tgz

ENV PATH $PATH:/sbt/bin

RUN sbt -V

WORKDIR /work

ENTRYPOINT ["bash"]
