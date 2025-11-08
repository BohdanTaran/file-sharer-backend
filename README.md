# FileSharer (Backend)

**Description**:  
A Spring Boot backend service that allows users to securely upload, manage, and share files through **AWS S3**.  
Each file can be uploaded as either **public** (accessible via a direct link) or **private** (restricted access).  
The application supports user authentication via **JWT**, metadata persistence in **PostgreSQL**,  
and real-time log aggregation via the **ELK stack** (Elasticsearch, Logstash, Kibana).

---

## **Features:**

- Upload files (up to 5MB) to **AWS S3**.  
- Choose between **public** and **private** visibility.  
- Generate and return the public S3 URL for shared files.  
- Store file metadata in **PostgreSQL**.  
- Authenticate users with **JWT** (login/register flow).  
- Centralized logging through **Logstash** → **Elasticsearch** → **Kibana**.  
- Dockerized setup for quick local development.

---

## **Tech Stack:**

- **Java 21**, **Spring Boot 3**
  - Spring Web, Spring Data JPA (Hibernate), Spring Security + JWT
- **PostgreSQL** for persistence
- **AWS S3 SDK v2** for file storage
- **Logback + Logstash Encoder** for structured JSON logging
- **Elasticsearch**, **Logstash**, **Kibana (ELK)** for log visualization
- **Docker & Docker Compose** for containerization
- **Swagger (Springdoc)** for REST API documentation

---

## **Setup & Installation:**

1. Clone the repository  
   ```bash
   git clone <repo-url>
   cd filesharer
   ```

2. Ensure **Docker** and **Docker Compose** are installed.

3. Configure your AWS credentials in the environment or `.env` file:
   ```
   AWS_ACCESS_KEY_ID=<your-access-key>
   AWS_SECRET_ACCESS_KEY=<your-secret-key>
   AWS_REGION=eu-central-1
   AWS_BUCKET_NAME=filesharer-bucket
   ```

4. Build and start all services:
   ```bash
   docker compose up --build
   ```

   This starts:
   - `backend` (Spring Boot app)
   - `db` (PostgreSQL)
   - `elasticsearch`, `logstash`, and `kibana`

5. The backend will run on:  
   👉 http://localhost:8080  
   Elasticsearch dashboard available at:  
   👉 http://localhost:5601

---

## **API Documentation:**

- **Swagger UI:**  
  👉 http://localhost:8080/q/swagger-ui/index.html#/

---

## **Logging & Monitoring:**

- All application logs are serialized in **JSON** and sent to **Logstash (port 5000)**.
- Logstash processes logs and forwards them to **Elasticsearch**, where they’re indexed by date (e.g. `filesharer-logs-2025.11.07`).
- Logs can be explored and visualized in **Kibana → Discover → FileSharer Logs**.

---

## **Usage:**

- **Authentication**
  - Register or login to get a JWT.
- **Upload a File**
  - `POST /api/files/upload`
  - Params:
    - `file`: the file (max 5MB)
    - `isPublic`: boolean (true/false)
- **Delete a File**
  - `DELETE /api/files?s3Key={fileName}`
- **Access File**
  - Public files are accessible directly via the returned S3 URL.
