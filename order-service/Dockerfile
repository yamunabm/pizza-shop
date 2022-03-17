FROM maven:latest as build
ADD . / order-service/
WORKDIR /order-service
RUN mvn clean install -U -DskipTests

FROM openjdk:11.0.7-jre-slim-buster
RUN mkdir order-service
WORKDIR /order-service/
COPY --from=build /order-service/target/order-service-0.0.1-SNAPSHOT.jar .

ENV PORT='8085'
EXPOSE ${PORT}

ENTRYPOINT /bin/sh -c "java -jar /order-service/order-service-0.0.1-SNAPSHOT.jar"