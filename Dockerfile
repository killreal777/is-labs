FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY target/is-labs-1.0.0.jar /app/is-labs-1.0.0.jar
ENTRYPOINT ["java", "-jar", "is-labs-1.0.0.jar"]
EXPOSE 8080