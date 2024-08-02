FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./build/libs/*.jar app.jar

EXPOSE 8090

ENV TZ Asia/Seoul

ENTRYPOINT ["java", "${JAVA_OPTS1}${JAVA_OPTS2}${JAVA_OPTS3}", "-jar", "-Dspring.profiles.active=dev /app.jar"]