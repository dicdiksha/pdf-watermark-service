ARG BASE_IMAGE=adoptopenjdk:11-jdk-hotspot

FROM docker:19.03.11 as static-docker-source
FROM ${BASE_IMAGE}

ARG SBT_VERSION=1.9.9
ARG SHA=0718a5afeac8c1464d33311f4de9617f63f9c88fbcbb5aba4e7b5aa56455f6b6
ARG BASE_URL=https://github.com/sbt/sbt/releases/download
COPY --from=static-docker-source /usr/local/bin/docker /usr/local/bin/docker

RUN apt-get update -qqy \
  && apt-get install -qqy curl bc unzip \
  && mkdir -p /usr/share \
  && curl -fsSL -o "sbt-${SBT_VERSION}.zip" "${BASE_URL}/v${SBT_VERSION}/sbt-${SBT_VERSION}.zip" \
  && unzip -qq "sbt-${SBT_VERSION}.zip" \
  && rm -f "sbt-${SBT_VERSION}.zip" \
  && mv sbt "/usr/share/sbt-${SBT_VERSION}" \
  && ln -s "/usr/share/sbt-${SBT_VERSION}/bin/sbt" /usr/bin/sbt \
  && apt-get remove -qqy --purge curl unzip \
  && rm /var/lib/apt/lists/*_*

WORKDIR /home
COPY . /home
ENTRYPOINT ["/usr/bin/bash", "sbt", "run"]
