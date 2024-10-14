FROM openjdk:17-jdk-slim
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTestsпше
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]