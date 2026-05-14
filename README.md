# Spring Boot Movies API with RestTemplate, Multithreading, H2, Spring Security, and JWT

This project is a Spring Boot REST API that consumes the HackerRank Movies API using RestTemplate. It fetches paginated movie data, supports filtering by page, movie name, and year, and secures movie endpoints using Spring Security with JWT-based authentication.
The project also stores users in an H2 in-memory database and uses a custom /auth/login API instead of Spring Security's default login page.

## Features
- Consume external movies API using RestTemplate
- Fetch paginated movie data
- Fetch all pages using multithreading
- Use ExecutorService and CompletableFuture for parallel API calls
- Filter movies by page, movie name, and year
- Store users in H2 in-memory database
- Encrypt passwords using BCrypt
- Custom user registration API
- Custom login API that returns JWT token
- JWT filter to validate token on protected APIs
- Store authenticated user identity in SecurityContextHolder
- Protect endpoints using @PreAuthorize
- H2 console enabled for local testing

## Tech Stack
- Java
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- H2 Database
- RestTemplate
- JWT
- Maven
- CompletableFuture
- ExecutorService

## External API Used
https://jsonmock.hackerrank.com/api/moviesdata/search/
