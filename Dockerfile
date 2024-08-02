FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./build/libs/*.jar app.jar

EXPOSE 8090

ENV TZ Asia/Seoul

ENTRYPOINT ["java", "-jar", "app.jar"]