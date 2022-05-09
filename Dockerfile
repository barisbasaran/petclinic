FROM openjdk:18

WORKDIR /app

COPY app-config.yml ./
COPY target/petclinic-dropwizard-1.0-SNAPSHOT.jar ./libs/
COPY target/dependency ./libs/

CMD ["java", "-cp", "libs/*", "io.baris.petclinic.dropwizard.PetclinicApplication", "server", "app-config.yml"]
