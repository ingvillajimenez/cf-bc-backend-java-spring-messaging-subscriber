# Use the official gradle/Java 17 image to create a build artifact.
# https://hub.docker.com/_/gradle
FROM gradle:jdk21-alpine AS build

# Copy local code to the container image.
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Build a release artifact.
RUN gradle bootJar --no-daemon

# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM eclipse-temurin:21-jdk-alpine

RUN mkdir /app

# Copy the jar to the production image from the builder stage.
COPY --from=build /home/gradle/src/build/libs/*.jar /app/subscriber-0.0.1-SNAPSHOT.jar

# Run the web service on container startup.
ENTRYPOINT ["java","-jar","/app/subscriber-0.0.1-SNAPSHOT.jar"]