# Pet Clinic with Dropwizard

## About 

This is an example **Java** RESTful web service for a **pet clinic**. 
It is mainly based on [Dropwizard](https://www.dropwizard.io) 
and [PostgreSQL](https://www.postgresql.org).  

The whole list of tools used is as follows:
* [Jakarta RESTful Web Services](https://en.wikipedia.org/wiki/Jakarta_RESTful_Web_Services)
* [Dropwizard](https://www.dropwizard.io)
* [Docker](https://www.docker.com)
* [PostgreSQL](https://www.postgresql.org)
* [Jdbi](https://jdbi.org)
* [Lombok](https://projectlombok.org)
* [Swagger](https://swagger.io)
* [Maven](https://maven.apache.org)

For testing:
* [JUnit](https://junit.org/junit5/)
* [Mockito](https://site.mockito.org/)
* [AssertJ](https://assertj.github.io/doc/)
* [Testcontainers](https://www.testcontainers.org)

## Installation

### Build project

Run `mvn package` to build project with _Maven_.


### Start application

Run `docker-compose --profile local up` to start application with _Docker_.

To check that your application is running enter url `http://localhost:8080/`

You may see application's health at `http://localhost:8081/healthcheck`

## Database Design

![](docs/design1.png)

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
