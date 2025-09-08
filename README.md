# 🚀 Cloud-Native API Orchestration System

A **cloud-native event-driven system** that integrates **Spring Boot (Java)** and **Node.js** services with AWS services like **API Gateway, SQS, SNS, DynamoDB, EventBridge, SES**.  
This project demonstrates **authentication/authorization, event orchestration, and async task processing** in a scalable, serverless-first architecture.

---

## 📌 Features

- **Authentication & Authorization**
  - Spring Boot `auth-service` with JWT & Spring Security (OAuth2.0 ready).
  - API Gateway integration for secure, role-based endpoint access.

- **Event Orchestration**
  - Events published to **Amazon EventBridge** from API requests.
  - Rules route events to **SQS** (task processing) and **SNS** (notifications).

- **Asynchronous Processing**
  - `task-processor` Lambda consumes **SQS** events and enriches data via external APIs.
  - Updates persisted in **DynamoDB**.
  - Notifications sent via **SES** (email) or **SNS** subscribers.

- **Infrastructure as Code**
  - AWS resources (Lambdas, SQS, SNS, DynamoDB, EventBridge) managed with **Serverless Framework**.
  - CI/CD via **GitHub Actions**.

---

## 🏗️ Architecture

```mermaid
flowchart LR
    subgraph User
      U[User] --> API
    end

    subgraph Auth-Service [Spring Boot Auth-Service]
      API[API Gateway] -->|JWT Auth| AUTH[Auth Service]
    end

    subgraph EventBus [AWS EventBridge]
      AUTH -->|Publish UserCreated| EB[(Event Bus)]
    end

    EB -->|Rule| Q[SQS Queue]
    EB -->|Rule| N[SNS Topic]

    subgraph TaskProcessor [Node.js Lambdas]
      Q --> L1[processTask Lambda]
      N --> L2[sendNotification Lambda]
    end

    L1 --> DB[(DynamoDB)]
    L2 --> E[SES Email]
