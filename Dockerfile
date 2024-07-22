#FROM openjdk:8-jdk-alpine
FROM amazoncorretto:8u422-alpine3.20-jre
ARG JAR_FILE
COPY ${JAR_FILE} spring-boot-complete.jar
ENTRYPOINT ["java","-jar","/spring-boot-complete.jar"]
