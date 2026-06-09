# Library Management System

A RESTful Library Management System built with **Java 17**, **Spring Boot 3**, **Hexagonal Architecture (Ports & Adapters)**, **Domain-Driven Design (DDD)**, and **MySQL**.

This application allows library administrators to manage borrowers and books, while supporting book borrowing and return operations.

---

# Features

## Borrower Management

- Register a new borrower
- Retrieve borrower details
- Unique email validation

## Book Management

- Register a new book
- List all books
- ISBN validation
- Support multiple copies of the same ISBN
- Each book has a unique Book ID

## Borrowing Management

- Borrow a book
- Return a borrowed book
- Prevent multiple borrowers from borrowing the same book simultaneously

---

# Technology Stack

| Technology | Version |
|------------|----------|
| Java | 17 |
| Spring Boot | 3.x |
| Spring Data JPA | Latest |
| MySQL | 8+ |
| Maven | 3.9+ |
| Lombok | Latest |
| OpenAPI / Swagger | Latest |
| Docker | Latest |
| Kubernetes | Latest |
| Helm | Latest |
| ArgoCD | Latest |

---

# Architecture

The project follows:

- Hexagonal Architecture
- Domain Driven Design (DDD)
- Clean Architecture principles

```text
src/main/java/com/library/api
│
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   ├── service
│   └── response
│
├── domain
│   ├── model
│   ├── valueobject
│   ├── exception
│   └── constant
│
├── infrastructure
│   ├── adapter
│   │   ├── in
│   │   └── out
│   └── config
│
└── LibrarySystemApplication
