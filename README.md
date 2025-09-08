# Cloud-Native API Orchestration (Starter)

Includes:
- `auth-service` (Spring Boot) — auth & JWT endpoints
- `task-processor` (Node.js Lambdas) — SQS consumer & SES notifications
- `serverless.yml` to deploy Node lambdas and infra
- GitHub Actions workflow for automated Node deployments

## Quick start (local dev)

### 1) Run auth-service locally
cd auth-service
mvn spring-boot:run
# service runs on http://localhost:8080
# endpoints:
# POST /auth/register {username,password,email}
# POST /auth/login {username,password}

### 2) Deploy Nodejs lambdas & infra
# configure AWS creds: aws configure (or set env vars)
cd task-processor
npm install
# deploy using root serverless.yml:
sls deploy --stage prod

## Notes
- Replace JWT secret in auth-service/ JwtUtil with secure value (SSM/SecretsManager).
- Verify SES sender before trying to send emails.
- This repo is a starter skeleton: add persistence (RDS/DynamoDB for users), better validation, and production-grade error handling.
