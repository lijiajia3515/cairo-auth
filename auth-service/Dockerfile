FROM openjdk:11-jre-slim

MAINTAINER lijiajia

RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list; \
    sed -i 's/security.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list; \
    apt clean -y; \
    apt update -y; \
    apt upgrade -y; \
    apt install curl wget jq -y

ADD build/libs/app.jar /hfhk/auth/service/app.jar

ENV TZ Asia/Shanghai
ENV LANG C.UTF-8
ENV LANGUAGE C.UTF-8
ENV LC_ALL C.UTF-8

ENV SPRING_PROFILES_ACTIVE docker


WORKDIR /hfhk/auth/service
VOLUME /hfhk/auth/service/logs

ENTRYPOINT java -jar app.jar

EXPOSE 80
HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 \
            CMD curl --silent --fail --request GET http://localhost/actuator/health | jq --exit-status '.status == "UP"' || exit 1
