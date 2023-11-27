FROM maven:3.8.3-openjdk-17 as builder

RUN mkdir -p /app
WORKDIR /app

# download maven dependencies
COPY pom.xml .
RUN mvn de.qaware.maven:go-offline-maven-plugin:resolve-dependencies

COPY src ./src
RUN mvn package

FROM ubuntu:20.04

# Install Microsoft fonts for Docx4J and JDK 17
# enable 'multiverse' repository and update package lists
RUN apt-get update && apt-get install -y software-properties-common && add-apt-repository multiverse
# set non-interactive mode and accept EULA for mscorefonts during installation
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y ttf-mscorefonts-installer openjdk-17-jdk
# update font cache
RUN fc-cache -f -v

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
