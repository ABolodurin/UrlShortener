FROM eclipse-temurin:17-jdk
COPY target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java" , "-jar", "application.jar"]
