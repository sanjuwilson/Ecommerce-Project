# Secure Event-Driven E-Commerce Backend

## Overview
This project is a **backend-only, microservices-based e-commerce platform**
built to demonstrate **secure authentication, distributed workflows,
and event-driven communication**.

The focus is on **backend architecture and system design**, not frontend UI.

---

## High-Level Architecture
- **API Gateway** – single entry point, JWT validation
- **Service Discovery** – dynamic service registration
- **Config Server** – centralized configuration
- **Authentication & Authorization** – Keycloak (OAuth2 + JWT)
- **Kafka** – asynchronous events and decoupled communication
- **PostgreSQL** – per-service databases (Dockerized)

---

## Roles & Security Model
- **Super Admin**
  - Can invite administrators via a secured endpoint
- **Admin**
  - Can manage users, orders, rewards, and system data
- **User**
  - Can register, authenticate, place orders, and make transactions

Authentication is handled by **Keycloak**.
All protected endpoints require **JWT tokens** issued by Keycloak and are
validated at the API Gateway.

---

## Core Functional Flow (In Scope)

### 1. Admin Onboarding
- Super-admin calls a secured `create-admin` endpoint
- Invitation email is sent
- Admin completes registration
- Admin data is persisted in the local database

### 2. User Registration & Activation
- User registers via `/register`
- User is stored in:
  - Local service database
  - Keycloak
- Password is **not set during registration**
- A password-creation email is sent
- After password setup, the user becomes active




### 3. Order Placement
- User authenticates and calls `/place-order`
- Order service validates request
- Order service makes a **Feign call** to Product service to:
  - Check stock availability
  - Update inventory
- Order is queued

### Cart (Optional Bulk Ordering)
Users may manage a cart using the Cart service:
- Add / update / remove items from an ACTIVE cart
- Cart service validates availability via Product service before accepting items
- On checkout, the Cart service converts cart items into an OrderRequest and calls the Order service
- Cart status is updated based on downstream processing (e.g., checkout, completion, deletion)

### 4. Payment & Transactions
- User chooses a payment method:
  - Gift Card
  - Reward Points
  - Credit/Debit (simulated)
- Transaction service processes the request
- On success:
  - Kafka event is published
  - Order status updates (e.g. QUEUED → COMPLETED)

### 5. Notifications (Event-Driven)
- Kafka events trigger the Notification service
- Emails are sent for:
  - Order confirmation
  - Payment confirmation
  - Reward points earned
  - Gift card delivery (QR code)

---

## Cart Service
- Users can:
  - Add / remove / update cart items
  - Checkout cart
- On checkout:
  - Cart is converted into an Order request
  - Order flow continues via `/place-order`

---

## Gift Card & Rewards System
- Gift cards are generated and tracked in the Transaction service
- Remaining balance is persisted and validated
- Rewards are earned per dollar spent
- Rewards and gift card delivery are handled asynchronously via Kafka

---

## Tech Stack
- **Language:** Java
- **Frameworks:** Spring Boot, Spring Cloud
- **Security:** Keycloak, OAuth2, JWT
- **Messaging:** Apache Kafka
- **Databases:** PostgreSQL
- **Inter-Service Communication:** OpenFeign
- **Infrastructure:** Docker, Docker Compose

---

## Running the Project (Local)

### Prerequisites
- Docker & Docker Compose
- Java 17+

### Steps
1. Start infrastructure:
   ```bash
   docker-compose up
