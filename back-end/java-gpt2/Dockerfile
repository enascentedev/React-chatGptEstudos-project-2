FROM maven:3.8.4 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM openjdk:17 AS jdk
COPY --from=build /usr/src/app/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]