## Booking System Application

This repository contains the source code for a Booking System application that simulates a property booking system. The application is built using Java Spring Boot framework and utilizes JWT (JSON Web Token) for security. The system includes two main entities: Person and Property.

## Features

User registration and authentication using JWT.
CRUD operations for managing persons and properties.
Ability to search for properties based on various criteria.
Booking functionality to reserve properties for a specific date range.
Price calculation depending on days reservation.
Reminder for reservation.
Coupon that will be applied to price calculation.
Access control to ensure only authenticated users can perform certain actions.
There are access control just for ADMIN.

## Technologies Used

Java Spring Boot
Spring Security
JSON Web Token (JWT)
MySQL 
Hibernate ORM
Maven (for dependency management)

## Prerequisites

Java JDK 11 or higher installed.
MySQL (or any other preferred database) installed and configured.
Maven installed for dependency management.

## Setup Instructions

1. Clone the repository:

bash
Copy code
git clone https://github.com/Sebi2899s/property-rezervation-system.git

2. Navigate to the project directory:

bash
Copy code
cd property-rezervation-system

3. Configure the database connection by modifying the application.properties file:

bash
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/property-rezervation-system
spring.datasource.username=db_username
spring.datasource.password=db_password

4. Build the project using Maven:

bash
Copy code
mvn clean install

5. Run the application:

bash
Copy code
mvn spring-boot:run

6. The application will be accessible at http://localhost:8080.

## API Endpoints
The following API endpoints are available for interacting with the application:

/auth/register (POST): Register a new user and retrive a jJWT token.

/auth/authenticate (POST): Authenticate user and retrieve JWT token.

/admin/get-users(GET): Only the admin can use this method to see all users that are registered.

/admin/get-properties(GET): Only the admin can use this method to see all properties that are registered.

/admin/add-user(POST): Admin can add/create a new user.

/admin/add-property(POST): Admin can add/create a new property.

/admin/update-role/{id}(PUT): Admin can change the role of a user.

/admin/remove-person/{id}(DELETE): Admin can remove a person.

/admin/remove-property/{id}(DELETE): Admin can remove a property.

/api/person/get-all(GET): Retrieve a list of all persons.

/api/person/{id} (GET): Retrieve a specific person by ID.

/api/person/excel(GET): Generate an excel with information from DB.

/api/person/save (POST): Create a new person.

/api/person/update/{id} (PUT): Update an existing person.

/api/person/delete/{id} (DELETE): Delete a person.

/api/person/{PersonId}/reservation/{PropertyId}(PUT): The method create a relation between a person and a property.

/api/property/get-all (GET): Retrieve a list of all properties.

/api/property/name(GET): Retrieve a property by a specific name.

/api/property/type(GET): Retrieve a property by its type.

/api/property/person-first-name(GET): Retrieve a property based on a person first name that has a relation with the property.

/api/property/excel(GET): Generate an excel raport with information about property from DB.

/api/property/{id} (GET): Retrieve a specific property by ID.

/api/property/save (POST): Create a new property.

/api/property/update/{id} (PUT): Update an existing property.

/api/property/delete{id} (DELETE): Delete a property.

/api/properties/search (POST): Search for properties based on criteria.

/api/bookings (POST): Create a new booking for a property.

Note: All requests (except /auth/register ) require a valid JWT token in the Authorization header.

Contributors

Sebi2899s
