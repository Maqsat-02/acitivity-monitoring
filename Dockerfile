FROM maven:3.8.3-openjdk-17 as builder

RUN mkdir -p /app
WORKDIR /app

# download maven dependencies
COPY pom.xml .
RUN mvn de.qaware.maven:go-offline-maven-plugin:resolve-dependencies

COPY src ./src
RUN mvn package

FROM maven:3.8.3-openjdk-17

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
