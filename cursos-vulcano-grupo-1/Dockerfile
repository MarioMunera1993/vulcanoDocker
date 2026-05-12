FROM eclipse-temurin:21-alpine AS build
WORKDIR /app

COPY pom.xml mvnw .
RUN chmod +x mvnw
COPY src src

RUN ./mvnw -B -DskipTests package -Dmaven.repo.local=/root/.m2/repository

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
