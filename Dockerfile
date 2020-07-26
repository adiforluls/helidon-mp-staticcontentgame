FROM maven:3.6-jdk-11 as build

WORKDIR /helidon

ADD pom.xml .
RUN mvn package -Dmaven.test.skip

ADD src src
RUN mvn package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /helidon

COPY --from=build /helidon/target/helidon-examples-microprofile-static-content.jar ./
COPY --from=build /helidon/target/libs ./libs

CMD ["java", "-jar", "helidon-examples-microprofile-static-content.jar"]

EXPOSE 8080
