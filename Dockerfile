FROM openjdk:11
WORKDIR /doro
COPY Doro-Server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
