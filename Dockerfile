#FROM openjdk:8-jdk-alpine
FROM openjdk:13-ea-jdk-alpine3.9
ARG JAR_FILE
COPY ${JAR_FILE} spring-boot-complete.jar
ENTRYPOINT ["java","-jar","/spring-boot-complete.jar"]
