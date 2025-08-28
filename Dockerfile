# ===== build =====
FROM maven:3.9.8-eclipse-temurin-11 AS build
WORKDIR /app
COPY . .
RUN mvn -q -DskipTests clean package spring-boot:repackage

# ===== run =====
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
