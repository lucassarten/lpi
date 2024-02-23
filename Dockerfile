FROM eclipse-temurin:21-jdk
EXPOSE 2066
ADD /target/lpi.jar lpi.jar
ENTRYPOINT ["java","-jar","lpi.jar"]
