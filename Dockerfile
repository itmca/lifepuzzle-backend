FROM openjdk:21

ARG JAR_PATH
COPY ${JAR_PATH} app.jar
CMD ["java", "-jar", "app.jar"]