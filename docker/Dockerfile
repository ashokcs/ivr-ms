FROM openjdk:8-alpine

ADD target/*.jar /app.jar

EXPOSE 80
ENTRYPOINT exec java -jar /app.jar