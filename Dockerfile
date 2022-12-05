FROM openjdk:11
COPY target/spring-boot-case-docker.jar spring-boot-case-docker.jar
ENTRYPOINT ["java","-jar","/spring-boot-case-docker.jar"]

