FROM openjdk:18

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# RUN ./mvnw dependency:go-offline

COPY src ./src
COPY app-config.yml lombok.config ./
COPY src/main/resources/database/db-init.sql ./

RUN ./mvnw -DskipTests clean package

CMD ["java", "-cp", "target/*:target/dependency/*", "io.baris.petclinic.PetclinicApplication", "server", "app-config.yml"]

#CMD ["tail", "-f", "pom.xml"]
