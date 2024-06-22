FROM maven:3.9.7-eclipse-temurin-21 as build
WORKDIR /app
COPY pom.xml .
COPY src/main src/main
COPY db/ db/
RUN mvn -f pom.xml clean package -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar glitterfin.jar
COPY --from=build /app/db db
ENTRYPOINT ["java","-jar","glitterfin.jar"]