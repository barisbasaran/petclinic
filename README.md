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
* [Mockito](https://site.mockito.org/)
* [AssertJ](https://assertj.github.io/doc/)
* [Testcontainers](https://www.testcontainers.org)

## Setup

You may install and start the database and the application with Docker.
 
`docker-compose up`

To check that your application is running enter url `http://localhost:8080/vets`

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
