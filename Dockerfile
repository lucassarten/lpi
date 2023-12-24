FROM eclipse-temurin:21-jdk
EXPOSE 80
ADD ./target/lpi.jar lpi.jar
ENTRYPOINT ["java","-jar","lpi.jar"]