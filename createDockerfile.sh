#!/usr/bin/env bash

DB_HOST=$1
DB_NAME=$2
DB_USER=$3
DB_PW=$4

cat > Dockerfile << END_TEXT
FROM openjdk:8-jre-alpine
MAINTAINER Xavier xavierpe@qq.com
COPY target/HyggeWriter.jar /HyggeWriter.jar
ENTRYPOINT [ "java", "-jar","-Dhost=${DB_HOST}","-DdbName=${DB_NAME}","-Dac=${DB_USER}","-Dpw=${DB_PW}","-Dspring.profiles.active=prod","/HyggeWriter.jar"]
END_TEXT