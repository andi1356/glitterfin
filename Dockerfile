FROM maven:3.9.7-eclipse-temurin-21 as build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
COPY db/ db/
RUN mvn -f pom.xml clean package

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar glitterfin.jar
ENTRYPOINT ["java","-jar","glitterfin.jar"]