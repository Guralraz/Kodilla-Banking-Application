# .:BACK-END DESIGN OF A SIMPLE APPLICATION FOR MANAGING A SMALL BANK BRANCH:.

The project was carried out by a Junior Java Developer. I created a back-end of a simple application for managing a bank branch, which I will continue to develop to increase its functionality and learn new things. Currently, I present what I have achieved.

## Table of contents

1. [Technologies used](#technologies-used)
2. [Assumptions](#assumptions)
3. [Getting Started](#getting-started)
4. [Api](#api)
5. [Further development](#further-development)

## Technologies Used
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::       (v2.7.1.RELEASE)
```

* Java 11
* Spring FrameWork
* Hibernate/Spring Data JPA
* Gradle
* Lombok
* JUnit/Jupiter
* MySQL
* Swagger2

## Assumptions

Roles:

Application user
- can register a new customer in the system;
- can register a user address;
- can open a new account for the user;
- can register a deposit or withdrawal to the user's account;
- can register a new loan for the user;
- can view the user's products (accounts, loans);
- can view the user's loan repayment schedule;
- may change or delete the user's address data;
- may deactivate or change some data of the user and its products;
- the system itself collects the purchase and sale values ​​ of currencies published by the NBP API.

Goals:

- A simple application for managing sales and customer service in a small bank branch
- Information about customers, their products and sales history in one place

## Getting started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
See deployment for notes on how to deploy the project on a live system. Just clone or download the files into a folder and it's ready to be used.

- Clone the repository
```
git clone https://github.com/GregRak/banking_app.git
```
- Build the project
```
./gradlew build
```
- Run the jar file on your local machine or in a docker container
  - Set the connection properties of your preferred database in the application.properties file

## API
You can check the full list of controllers in a friendly form via the Swagger2 library. To do this, run the application locally and enter this address in the web browser:
http://localhost:8080/swagger-ui.html#/

### Status Codes

This app returns the following status codes in most of its API:

| Status Code | Description |
| :--- | :--- |
| 200 | `OK` |
| 201 | `CREATED` |
| 400 | `BAD REQUEST` |
| 403 | `FORBIDDEN` |
| 404 | `NOT FOUND` |
| 500 | `INTERNAL SERVER ERROR` |

Any differences shall be displayed with the discribed api.

## Further development

Currently, the application allows for a large section of the possibility of managing a bank branch. 
Nevertheless, major development is planned in the following areas:
- adding a service updating the account balance after posting the operation,
- adding a service updating the status of the loan and installment after repayment,
- adding a service that checks any arrears and generates an alert and email, if there is such a backlog,
- adding a module to open a term deposit.
It is planned to rewrite the application in microservice technology at a later date.