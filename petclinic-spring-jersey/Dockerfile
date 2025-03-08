FROM openjdk:18

WORKDIR /app

COPY target/petclinic-spring-jersey-1.0-SNAPSHOT.jar ./libs/
COPY target/dependency ./libs/

CMD ["java", "-Dspring.profiles.active=docker", "-cp", "libs/*", "io.baris.petclinic.springjersey.PetclinicApplication"]
