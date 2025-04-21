FROM openjdk:21-jdk
ADD app/build/libs/app.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java","-jar", "app.jar"]