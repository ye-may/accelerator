FROM gradle:7.6.3-jdk17 AS BUILD_IMAGE
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY . .
RUN ./gradlew clean build



FROM eclipse-temurin:17-jdk-centos7
ENV ARTIFACT_NAME=uromy-0.0.1.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=BUILD_IMAGE $APP_HOME/build/libs/*.jar ${ARTIFACT_NAME}

EXPOSE 8080
ENTRYPOINT exec java -jar -Dspring.profiles.active=${API_SPRING_PROFILE} ${ARTIFACT_NAME}
