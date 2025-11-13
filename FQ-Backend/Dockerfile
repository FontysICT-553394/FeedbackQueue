# -- Build --
FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /workspace
COPY . .
RUN gradle --no-daemon clean quarkusBuild

# -- Runtime --
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Configurable port (defaults to 8080)
ARG PORT=8080
ENV QUARKUS_HTTP_PORT=${PORT}
EXPOSE ${PORT}

# Copy Quarkus fast-jar layout
COPY --from=build /workspace/build/quarkus-app/ /app/

# Run the app
ENTRYPOINT ["sh","-c","java -jar /app/quarkus-run.jar"]
