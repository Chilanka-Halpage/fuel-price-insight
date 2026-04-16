# fuel-price-insight
AI-powered fuel price monitoring and prediction platform built with Spring Boot, Angular, integrating real-time APIs and user feedback.

<img width="968" height="930" alt="Screenshot 2026-04-16 at 12 22 43" src="https://github.com/user-attachments/assets/1de99e07-27d6-4ec8-870a-844a31e12803" />
<img width="968" height="922" alt="image" src="https://github.com/user-attachments/assets/e79fa004-a8c1-443f-adc5-ca6361e5cf70" />
<img width="968" height="819" alt="Screenshot 2026-04-16 at 14 13 40" src="https://github.com/user-attachments/assets/192a46ec-2b64-490c-9463-614e6d1d9daf" />
<img width="1917" height="929" alt="Screenshot 2026-04-16 at 12 12 18" src="https://github.com/user-attachments/assets/060eebae-fda3-464b-82a4-297fe2ae97a0" />
<img width="1046" height="709" alt="Screenshot 2026-04-16 at 11 29 44" src="https://github.com/user-attachments/assets/382459c1-09ed-4cb4-af46-31650cf10086" />
<img width="579" height="375" alt="Screenshot 2026-04-16 at 12 15 30" src="https://github.com/user-attachments/assets/fd3c8990-5849-4937-8b33-a40222c55be8" />
<img width="579" height="383" alt="Screenshot 2026-04-16 at 12 16 09" src="https://github.com/user-attachments/assets/ac0188d1-05ae-4764-9d5e-47c2edf28ccf" />


## Overview

The **Fuel Price Insight Application** is a full-stack, microservices-based platform designed to provide users with:

- Real-time fuel price information  
- AI-powered price predictions  
- Historical price trends  
- Community-driven discussions  

It follows a **cloud-native microservices architecture** using Spring Boot and Angular, ensuring scalability, resilience, and maintainability.

---

## Architecture

The system is built using a **microservices architecture** with the following components:

### Backend Microservices (Spring Boot)

#### API Gateway
- Central entry point for all client requests  
- Routes requests to appropriate services  
- Secures APIs using JWT validation (Keycloak)  
- Implements logging and request filtering  

#### Config Server
- Centralized configuration management for all microservices  

#### Eureka Server
- Service discovery and registration for microservices  

#### Fuel Service
- Provides:
  - Current fuel prices  
  - AI-based predicted fuel prices  
  - Historical price data  
- Uses **PostgreSQL** for persistence  
- Uses **Infinispan cache** for performance and fault tolerance
- Uses **Flyway** for schema creation
- Integrates with **Oil Price API**
- Implements:
  - Circuit Breaker  
  - Retry mechanism  
  - Fallback strategies  

#### AI Service
- Generates fuel price predictions  
- Integrates with **Google Gemini AI**  

#### Price Comment Service
- Accepts user comments  
- Publishes messages to **RabbitMQ**  

#### Comment Manager Service
- Consumes messages from **RabbitMQ**  
- Persists comments in **MongoDB**  
- Exposes APIs to retrieve comments  

---

### Frontend (Angular)

- Built using **Angular**
- UI powered by **Angular Material**

#### Features:
- User authentication 
- View current, predicted, and historical fuel prices  
- Post and view comments  

---

## Security

Authentication and authorization are handled using **Keycloak**.

### OAuth2 Flow
- Authorization Code Flow with PKCE  
- JWT-based authentication

### Frontend Security
Using `angular-oauth2-oidc`:
- Login / Logout  
- Token refresh  
- Secure API communication  

### Backend Security
- API Gateway acts as **Resource Server**  
- Validates JWT tokens for all requests  

---

## Messaging & Communication

### RabbitMQ
- Used for asynchronous communication  
- Decouples comment creation and processing  
- Ensures scalability and reliability  

---

## Data Storage

- **PostgreSQL** → Fuel price data  
- **MongoDB** → User comments  
- **Infinispan** → Distributed caching layer  

---

## Observability

### Zipkin
- Distributed tracing across all microservices  
- Helps monitor request flow and latency  

---

## Containerization

The following services are containerized using **Docker**:

- RabbitMQ  
- PostgreSQL  
- Keycloak  
- Zipkin  

---

## Key Features

- Real-time fuel price retrieval  
- AI-powered price prediction  
- Historical price tracking  
- User comment system  
- Secure authentication & authorization  
- Resilient microservices (Retry, Circuit Breaker, Fallback)  
- Asynchronous processing with RabbitMQ  
- Distributed tracing with Zipkin
- Schema management with Flyway
- Centralized configuraion with Config Server
- Service discovery and load balancing with Eureka 

---

## Technologies Used

### Backend
- Spring Boot  
- Spring Cloud (Eureka, Config Server, Gateway)  
- Spring Security (OAuth2 Resource Server)  
- Resilience4j  
- RabbitMQ  

### Frontend
- Angular  
- Angular Material  
- angular-oauth2-oidc  

### Databases
- PostgreSQL  
- MongoDB  

### DevOps & Tools
- Docker  
- Zipkin  

---

## Project Goal

To build a **scalable, secure, and intelligent fuel price insight platform** using modern microservices architecture and cloud-native practices.
