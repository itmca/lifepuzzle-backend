FROM openjdk:21

ARG JAR_PATH=services/lifepuzzle-api/build/libs/lifepuzzle-api-*.jar
COPY ${JAR_PATH} app.jar
CMD ["java", "-jar", "app.jar"]