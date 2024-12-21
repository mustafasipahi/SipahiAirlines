FROM maven:3.8.6-amazoncorretto-11 AS maven_build
WORKDIR /sipahi_airlines_app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM openjdk:11
WORKDIR /sipahi_airlines_app
COPY --from=maven_build /sipahi_airlines_app/target/"SipahiAirlines-1.0-SNAPSHOT.jar" sipahi_airlines_app.jar
ENTRYPOINT ["java", "-Dserver.address=0.0.0.0", "-jar", "sipahi_airlines_app.jar"]
EXPOSE 8080