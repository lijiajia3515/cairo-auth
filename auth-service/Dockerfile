FROM openjdk:11.0.8-jre-slim

MAINTAINER lijiajia

RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list; \
    sed -i 's/security.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list; \
    apt clean -y; \
    apt update -y; \
    apt upgrade -y

ENV TZ Asia/Shanghai
ENV LANG C.UTF-8
ENV LANGUAGE C.UTF-8
ENV LC_ALL C.UTF-8

ENV SPRING_PROFILES_ACTIVE docker

WORKDIR /hfhk/auth/service

ADD build/libs/app.jar /hfhk/auth/service/app.jar

VOLUME /hfhk/auth/service/logs

EXPOSE 80

ENTRYPOINT java -jar app.jar
