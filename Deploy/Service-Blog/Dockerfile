FROM openjdk:8-jdk-alpine
MAINTAINER Xavier xavierpe@qq.com
COPY *.jar /service-blog.jar

ENV dbHost="mysql:3306"
ENV dbName="main"
ENV dbAc="root"
ENV dbPw="root"
ENV esHost="elasticsearch:9200"

ENV JVM_OPTS="-Xmx1344M -Xms1344M -Xmn448M -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:ErrorFile=/logFile/hs_err_pid%p.log   -Xloggc:/logFile/gc.log -XX:HeapDumpPath=/logFile -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintClassHistogramBeforeFullGC -XX:+PrintClassHistogramAfterFullGC -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCApplicationStoppedTime -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC"

ENTRYPOINT ["/bin/sh","-c","java $JVM_OPTS -jar -Dhost=${dbHost} -DdbName=${dbName} -Dac=${dbAc} -Dpw=${dbPw} -DesHost=${esHost} -Dspring.profiles.active=prod service-blog.jar"]