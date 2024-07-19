FROM amazoncorretto:21.0.3-alpine
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
LABEL authors="ghost_of_endless"