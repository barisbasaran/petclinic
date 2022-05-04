FROM openjdk:18

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY src ./src
COPY app-config.yml lombok.config ./

RUN ./mvnw -DskipTests clean package

CMD ["java", "-cp", "target/*:target/dependency/*", "io.baris.petclinic.PetclinicApplication", "server", "app-config.yml"]
