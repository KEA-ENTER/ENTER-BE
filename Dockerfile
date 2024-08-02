FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./build/libs/*.jar app.jar

EXPOSE 8090

ENV TZ Asia/Seoul

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS1} ${JAVA_OPTS2} ${JAVA_OPTS2} -jar -Dspring.profiles.active=dev /app.jar"]