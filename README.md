# Pet Clinic

## About 

This is an example **RESTful** web service for the **Java Petclinic**.

It is built using the following tools:
* [Jakarta RESTful Web Services](https://en.wikipedia.org/wiki/Jakarta_RESTful_Web_Services)
* [Dropwizard](https://www.dropwizard.io)
* [PostgreSQL](https://www.postgresql.org)
* [Jdbi](https://jdbi.org)
* [Lombok](https://projectlombok.org)
* [Swagger](https://swagger.io)
* [Maven](https://maven.apache.org)

It is using the following test frameworks:
* [JUnit](https://junit.org/junit5/)
* [mockito](https://site.mockito.org/)
* [AssertJ](https://assertj.github.io/doc/)
* [Testcontainers](https://www.testcontainers.org)

## Setup

### Install PostgreSQL

Install and start the postgre Docker image

`docker run --name local-postgres -e POSTGRES_PASSWORD=sa -d -p 5432:5432 postgres:latest`

Connect to the Docker container

`docker exec -it local-postgres bash`

Connect to the postgre CLI

`psql -U postgres`

Create a database called **mydb**

`CREATE DATABASE mydb;`

### How to start

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/petclinic-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

### Health check

To see your application's health enter url `http://localhost:8081/healthcheck`

## Endpoints

`GET /pets` Get all pets

`PUT /pets` Create pet

`GET /pets/{pet_id}` Get pet

`POST /pets/{pet_id}` Update pet

`GET /vets` Get all vets

`PUT /vets` Create vet

`GET /vets/{vet_id}` Get vet

`POST /vets/{vet_id}` Update vet

`PUT /visits/pets/{pet_id}/vets/{vet_id}` Make visit to the vet

`GET /visits/pets/{pet_id}` Get visits of the pet
