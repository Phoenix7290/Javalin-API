FROM openjdk:21-jdk-slim
ENV LANG=C.UTF-8
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar"]