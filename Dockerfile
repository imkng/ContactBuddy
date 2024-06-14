FROM openjdk:17

COPY target/api_application.jar api_application.jar

ENTRYPOINT ["java", "-jar", "api_application.jar"]
