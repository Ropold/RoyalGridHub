FROM --platform=linux/amd64 openjdk:21
LABEL authors="ropold"
EXPOSE 8080
COPY backend/target/royalgridhub.jar royalgridhub.jar
ENTRYPOINT ["java", "-jar", "royalgridhub.jar"]