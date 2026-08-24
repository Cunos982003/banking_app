# build stage
FROM maven:3.9.4-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml mvnw* ./
COPY src ./src
RUN mvn -B -DskipTests package

# runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/transaction-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
