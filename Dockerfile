FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/PRMProject-0.0.2.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]