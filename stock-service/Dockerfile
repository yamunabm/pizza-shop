FROM maven:latest as build
ADD . / stock-service/
WORKDIR /stock-service
RUN mvn clean install -U -DskipTests

FROM openjdk:11.0.7-jre-slim-buster
RUN mkdir stock-service
WORKDIR /stock-service/
COPY --from=build /stock-service/target/stock-service-0.0.1-SNAPSHOT.jar .

ENV PORT='8085'
EXPOSE ${PORT}

ENTRYPOINT /bin/sh -c "java -jar /stock-service/stock-service-0.0.1-SNAPSHOT.jar"