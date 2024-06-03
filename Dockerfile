FROM openjdk:17
COPY target/my-app.jar my-app.jar
ENTRYPOINT ["java", "-jar", "my-app.jar"]

