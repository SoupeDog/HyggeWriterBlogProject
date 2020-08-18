#!/usr/bin/env bash

DB_HOST=$1
DB_NAME=$2
DB_USER=$3
DB_PW=$4

cat > Dockerfile <<END_TEXT
FROM openjdk:8-jdk-alpine
MAINTAINER Xavier xavierpe@qq.com
COPY *.jar /service-blog.jar

ENV JVM_OPTS="-Xmx70M -Xms70M -Xmn20M -Xloggc:/logFile/gc.log -XX:+ParallelRefProcEnabled -XX:ErrorFile=/logFile/hs_err_pid%p.log -XX:HeapDumpPath=/logFile -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintClassHistogramBeforeFullGC -XX:+PrintClassHistogramAfterFullGC -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCApplicationStoppedTime -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:MaxMetaspaceSize=64M -XX:MetaspaceSize=64M -XX:+UseParallelGC -XX:+UseAdaptiveSizePolicy -XX:MaxGCPauseMillis=100"

ENTRYPOINT ["/bin/sh","-c","java \$JVM_OPTS -jar -Dhost=${DB_HOST} -DdbName=${DB_NAME} -Dac=${DB_USER} -Dpw=${DB_PW} -Dspring.profiles.active=prod service-blog.jar"]
END_TEXT