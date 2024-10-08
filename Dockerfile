FROM openjdk:17-jdk-slim
COPY "./frontend-service-1.7.0.jar" "app.jar"
EXPOSE 9092
ENTRYPOINT ["java","-Xms512m","-jar","app.jar"]
