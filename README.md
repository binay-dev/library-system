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

# GitOps Deployment (Argo CD + Helm + Kubernetes)
This project demonstrates a GitOps-based deployment pipeline for a Spring Boot microservice using:
Spring Boot 3.x
MySQL (Kubernetes Deployment)
Docker
Helm Charts
GitHub Actions (CI)
Argo CD (CD)
Kubernetes
<img width="1493" height="627" alt="Screenshot 2026-06-12 224557" src="https://github.com/user-attachments/assets/45ba7240-8a73-4a9a-8818-bddc6d652b0a" />

# Flow Git Push (main)
   ↓
GitHub Actions (CI)
   ↓
Build JAR + Docker Image
   ↓
Push Image to Docker Hub
   ↓
Argo CD (GitOps)
   ↓
Helm Chart Deployment
   ↓
Kubernetes Cluster

## 📁 Project Structure

```text
library-system/
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── helm/
│   ├── Chart.yaml
│   ├── values.yaml
│   └── templates/
│       ├── deployment.yaml
│       ├── service.yaml
│       ├── hpa.yaml
│       ├── ingress.yaml
│       └── httproute.yaml
├── argocd/
     ├── library-system-app.yaml
├── k8s/
├── src/
│   └── main/java/...
│
├── Dockerfile
└── pom.xml
```
