# Spring Boot Testing Project Reference and Tutorial

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Project Setup](#2-project-setup)
3. [RESTful APIs](#3-restful-apis)
4. [CRUD and Business Operations](#4-crud-and-business-operations)
5. [Import and Export Functionality](#5-import-and-export-functionality)
6. [Job Functionality](#6-job-functionality)
7. [PDF Receipt Generation](#7-pdf-receipt-generation)
8. [Notification Functionality](#8-notification-functionality)
9. [Spring Boot Testing](#9-spring-boot-testing)
10. [API Testing Guide](#10-api-testing-guide)
11. [Code Structure and Design](#11-code-structure-and-design)
12. [End-to-End Workflows](#12-end-to-end-workflows)
13. [Troubleshooting and Common Issues](#13-troubleshooting-and-common-issues)
14. [Quick Reference](#14-quick-reference)
15. [Future Extensions](#15-future-extensions)

---

## 1. Project Overview

This project is a Spring Boot learning and reference application built to practice and demonstrate common backend capabilities in a compact, hands-on way.

It is not a large enterprise system. Instead, it acts as a practical sandbox for learning:

- RESTful API design and controller structure
- Spring MVC request handling
- JPA + MySQL persistence
- File upload, import, and export
- PDF generation
- Async job execution
- Firebase push notification basics
- Spring Boot configuration and environment profile usage
- Testing fundamentals and project bootstrapping

### Purpose of the project

The application focuses on a single domain: user management and support features around user data, file processing, and notification demos. The idea is to make it easy to test and understand how the pieces fit together without digging through a large application.

### Technologies demonstrated

The implementation uses the following stack:

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA and JDBC
- MySQL
- Thymeleaf
- Apache POI (Excel)
- Apache Commons CSV
- OpenPDF and OpenHTMLToPDF
- Firebase Admin SDK
- AWS S3, Google Cloud Storage, Azure Blob clients (optional configuration paths)
- Spring Security (disabled for all requests)
- Spring Scheduling and Async
- Actuator scheduled tasks endpoint

### Architecture and overall flow

The application is organized into a simple layered design:

1. Controller layer
   - Handles HTTP requests
   - Exposes REST endpoints and HTML views
2. Service layer
   - Contains business logic such as user creation, PDF generation, CSV/Excel import/export
3. Repository layer
   - Uses Spring Data JPA repositories such as `UserRepo` and `UserTokenRepo`
4. Model / entity layer
   - `User` and `UserToken` are the main JPA entities
5. DTO layer
   - Request payloads such as `SetUserReq`, `ImportUserReq`, and `NotifyReq`
6. Utility and config layer
   - `Helper`, `Resp`, config classes, storage configuration, and Firebase initialization

A typical request flow looks like this:

- Client sends HTTP request to a controller
- Controller validates or parses request data
- Controller calls a service method
- Service reads or saves `User` or `UserToken` data via repository
- Response is wrapped in `ApiResp` and sent back with a payload
- For files or PDFs, the response may include a downloadable attachment instead of JSON

### How this is useful as a quick Spring Boot testing reference

This project is intentionally practical:

- It exposes many REST endpoints to test quickly in Postman or curl
- It includes file-based imports/exports and PDF downloads
- It includes notification flow examples to study FCM usage
- It shows how async jobs and scheduled tasks are wired in Spring Boot
- It includes configuration examples for multiple storage backends

This is useful as a reference app when learning how to connect a controller, service, repository, and database in a real Spring Boot application.

---

## 2. Project Setup

### Prerequisites

Before running this project, make sure you have:

- Java 21 installed
- Maven installed or use the included `mvnw` wrapper
- MySQL running locally
- A Firebase service account JSON file for FCM, if you want notification testing
- Optional cloud storage credentials for S3, GCP, or Azure

### Main project dependencies

The project is configured in [pom.xml](../pom.xml). The key dependencies include:

- `spring-boot-starter-webmvc`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-data-jdbc`
- `spring-boot-starter-validation`
- `spring-boot-starter-security`
- `spring-boot-starter-thymeleaf`
- `spring-boot-starter-actuator`
- `mysql-connector-j`
- `openpdf`
- `poi` and `poi-ooxml`
- `commons-csv`
- `firebase-admin`
- `software.amazon.awssdk:s3`
- `com.google.cloud:google-cloud-storage`
- `com.azure:azure-storage-blob`

### Running the project

From the project root:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

On Windows, this can also be run using the provided script or Maven command:

```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

### Application configuration

The main configuration file is:

- [src/main/resources/application.properties](../src/main/resources/application.properties)

Key settings:

```properties
spring.application.name=demo
server.port=8080
spring.profiles.active=dev
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
storage.mode=local
storage.local.path=assets/uploads
fcm.notify=true
management.endpoints.web.exposure.include=scheduledtasks
```

### Profile-based configuration

The project includes environment files:

- [src/main/resources/application-dev.properties](../src/main/resources/application-dev.properties)
- [src/main/resources/application-prod.properties](../src/main/resources/application-prod.properties)
- [src/main/resources/application-test.properties](../src/main/resources/application-test.properties)

The default dev config points to MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/first_spring?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
```

This means the database should exist or be creatable when the app starts.

### Storage configuration

The project supports several storage modes through `storage.mode`:

- `local` (default)
- `s3`
- `azure`
- `gcp`

The storage backend is selected by `StorageFactory` and the matching configuration class:

- `LocalStorageService`
- `AwsS3StorageService`
- `AzureStorageService`
- `GcpStorageService`

### Firebase configuration

Notification support requires Firebase Admin configuration.

The app loads credentials from:

- [src/main/resources/firebase-service-account.json](../src/main/resources/firebase-service-account.json)

Initialized in:

- [src/main/java/com/akashf/springv4/demo/config/FCMConfig.java](../src/main/java/com/akashf/springv4/demo/config/FCMConfig.java)

The property `fcm.notify=true` enables notification initialization.

### Security note

The app config disables CSRF and permits all requests in:

- [src/main/java/com/akashf/springv4/demo/config/SecurityConfig.java](../src/main/java/com/akashf/springv4/demo/config/SecurityConfig.java)

This is useful for quick API testing, but it is not a production security setup.

---

## 3. RESTful APIs

The project exposes REST endpoints in multiple controllers.

### 3.1 Hello and localization endpoints

#### GET /hello

Controller:

- [src/main/java/com/akashf/springv4/demo/controller/HelloController.java](../src/main/java/com/akashf/springv4/demo/controller/HelloController.java)

Purpose:
- Demonstrates Spring `MessageSource` and locale-based message resolution.

Example:

```bash
curl -H "Accept-Language: hi" http://localhost:8080/hello
```

Example response:

```text
स्वागत है Akash Fulari
```

The default locale file is:

- [src/main/resources/locales/lang_en.properties](../src/main/resources/locales/lang_en.properties)

and additional locales are present in the same directory.

---

### 3.2 User management API

Controller:

- [src/main/java/com/akashf/springv4/demo/controller/UserController.java](../src/main/java/com/akashf/springv4/demo/controller/UserController.java)

Base path:

```text
/user
```

#### POST /user

Creates a user.

Request:
- multipart form-data
- fields: `name`, `email`, `avatar`

Headers:

```text
Content-Type: multipart/form-data
```

Example:

```bash
curl -X POST http://localhost:8080/user \
  -F "name=John Doe" \
  -F "email=john@example.com" \
  -F "avatar=@/path/to/avatar.png"
```

Success response shape:

```json
{
  "success": true,
  "message": "Created",
  "info": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "avatar": "asstes/users/profile/filename.png"
  },
  "timestamp": "2026-08-14T10:00:00"
}
```

Validation:
- `name` must be 3-50 characters and not blank
- `email` must be valid and not blank

---

#### PUT /user/{userId}

Updates an existing user.

Example:

```bash
curl -X PUT http://localhost:8080/user/1 \
  -F "name=John Updated" \
  -F "email=john.updated@example.com" \
  -F "avatar=@/path/to/new-avatar.png"
```

Behavior:
- Looks up the user by id
- Replaces the name and email
- Uploads and stores the new avatar if provided
- Deletes old avatar when replaced

---

#### POST /user/list

Gets paginated user data from a request body.

Request body:

```json
{
  "page": 0,
  "size": 10,
  "sortBy": "id",
  "sortDirection": "ASC"
}
```

The DTO is:

- [src/main/java/com/akashf/springv4/demo/dto/PaginReq.java](../src/main/java/com/akashf/springv4/demo/dto/PaginReq.java)

Success response shape:

```json
{
  "success": true,
  "message": "Fetched List",
  "items": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "avatar": "asstes/users/profile/filename.png"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 10,
    "totalItems": 1,
    "totalPages": 1,
    "first": true,
    "last": true,
    "hasNext": false,
    "hasPrevious": false
  }
}
```

---

#### GET /user/list

Gets paginated user data using Spring `Pageable` directly.

Example:

```bash
curl "http://localhost:8080/user/list?page=0&size=10&sort=id,asc"
```

---

#### GET /user/{userId}

Fetches a single user by ID.

Example:

```bash
curl http://localhost:8080/user/1
```

If the avatar path is valid, the service converts it into a URL using `Helper.GetURL(...)` and current request path context.

---

#### DELETE /user/{userId}

Deletes a user by id.

Example:

```bash
curl -X DELETE http://localhost:8080/user/1
```

Response shape:

```json
{
  "success": true,
  "message": "Deleted Info"
}
```

---

### 3.3 PDF report endpoints

#### GET /user/{userId}/report

Generates a PDF using raw content generation with `com.lowagie.text`.

Behavior:
- Looks up the user
- Creates a PDF with a simple profile summary
- Returns a file download with `Content-Disposition: attachment; filename=user-profile.pdf`
- Uses `application/pdf`

Example:

```bash
curl -L -o user-report.pdf http://localhost:8080/user/1/report
```

#### GET /user/{userId}/report2

Generates a PDF using an HTML template.

Behavior:
- Resolves the user
- Renders a Thymeleaf template
- Converts the HTML to PDF using `openhtmltopdf`
- Returns a PDF file download

Template used:

- [src/main/resources/templates/pdf/temp2.html](../src/main/resources/templates/pdf/temp2.html)

---

### 3.4 Export endpoints

#### GET /user/export/{type}

Exports users in either Excel or CSV.

Path variable:
- `type` is an enum of `ReportType`
- valid values: `EXCEL`, `CSV`

Example:

```bash
curl -L -o users.csv http://localhost:8080/user/export/CSV
curl -L -o users.xlsx http://localhost:8080/user/export/EXCEL
```

The response content type is:
- Excel: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- CSV: `text/csv`

The output file name is:
- `users-report.csv`
- `users-report.xlsx`

#### POST /user/export

Exports data based on a JSON request body.

Request body:

```json
{
  "page": 0,
  "size": 10,
  "sortBy": "id",
  "sortDirection": "ASC",
  "type": "EXCEL"
}
```

This uses `ExportUserReq`, which extends `PaginReq`.

---

### 3.5 Import endpoints

#### POST /user/import

Imports users from an Excel or CSV file.

Request:
- multipart form-data
- fields: `file`, `type`

Example:

```bash
curl -X POST http://localhost:8080/user/import \
  -F "type=CSV" \
  -F "file=@/path/to/users.csv"
```

Example CSV template:

- [src/main/resources/report/templates/import-users.csv](../src/main/resources/report/templates/import-users.csv)

Import flow:
- `UserImportExport.importCsv(...)` parses rows with Apache Commons CSV
- `UserImportExport.importExcel(...)` reads an XLSX workbook and saves rows as `User` objects

Success response:

```json
{
  "success": true,
  "message": "Imported successfully!"
}
```

---

### 3.6 Global helper endpoints

#### GET /download-template/{template}/{type}

Downloads a report template file.

Path variables:
- `template`: `IMPORT_USERS`
- `type`: `EXCEL` or `CSV`

Example:

```bash
curl -L -o import-users.csv http://localhost:8080/download-template/IMPORT_USERS/CSV
```

This loads a file from:

- [src/main/resources/report/templates/import-users.csv](../src/main/resources/report/templates/import-users.csv)

---

#### POST /test-fcm

Sends a Firebase Cloud Messaging notification.

Request body:

```json
{
  "targetToken": "DEVICE_FCM_TOKEN",
  "title": "Hello",
  "body": "This is a test notification"
}
```

This invokes `Helper.notifyFCM(req)` and returns a success or error message.

---

#### POST /token/save

Saves a device token to the database.

Request body:

```json
{
  "token": "FCM_TOKEN_STRING",
  "device": "WEB"
}
```

The `device` field is an enum with values:

- `ANDROID`
- `IOS`
- `WEB`

On success:

```json
{
  "success": true,
  "message": "Token saved successfully",
  "info": {
    "id": 1,
    "token": "FCM_TOKEN_STRING",
    "device": "WEB"
  }
}
```

#### GET /token/latest

Fetches the latest token stored in the database.

Example:

```bash
curl http://localhost:8080/token/latest
```

Success response:

```json
{
  "success": true,
  "message": "Latest token fetched successfully",
  "info": {
    "id": 1,
    "token": "FCM_TOKEN_STRING",
    "device": "WEB"
  }
}
```

If no token exists, the controller returns an error message: `No token found`.

---

### 3.7 Job trigger endpoint

Controller:

- [src/main/java/com/akashf/springv4/demo/controller/JobController.java](../src/main/java/com/akashf/springv4/demo/controller/JobController.java)

#### GET /jobs/test/{taskName}

Example:

```bash
curl http://localhost:8080/jobs/test/cleanup
```

Behavior:
- Calls `jobWorker.processHeavyJob(taskName)`
- Method is annotated with `@Async("jobExecutor")`
- The HTTP request returns immediately with a success message
- The job runs in the background executor

Response:

```text
Job submitted successfully! It is now running in the background queue.
```

---

## 4. CRUD and Business Operations

### User creation and update

The user entity is defined as:

- [src/main/java/com/akashf/springv4/demo/model/User.java](../src/main/java/com/akashf/springv4/demo/model/User.java)

Fields:

- `id`
- `name`
- `email`
- `avatar`

The repository is:

- [src/main/java/com/akashf/springv4/demo/repository/UserRepo.java](../src/main/java/com/akashf/springv4/demo/repository/UserRepo.java)

The service is:

- [src/main/java/com/akashf/springv4/demo/service/UserService.java](../src/main/java/com/akashf/springv4/demo/service/UserService.java)

Important flow:

1. Request arrives at `UserController`
2. `@ModelAttribute SetUserReq` binds form fields
3. `UserService.createUser(...)` calls `setUser(...)`
4. If avatar exists, `Helper.UploadTo(...)` writes the file to `asstes/users/profile/`
5. The user is saved with `repo.save(...)`
6. Response is wrapped with `Resp.sucess(...)`

### User read and pagination

`UserService.getAllUsers(Pageable pageable)` calls `repo.findAll(pageable)`.

This allows retrieval of page-based results and metadata such as:

- page
- size
- totalItems
- totalPages
- first/last page flags

### User delete

`UserService.deleteUser(Long id)` calls `repo.deleteById(id)`.

### User report generation

`UserService.userRawReportPdf(Long userId)` creates a raw PDF using lowagie `Document` and `PdfWriter`.

`UserService.userTemplateReportPdf(Long userId)` renders a Thymeleaf HTML template and converts it into a PDF with `PdfRendererBuilder`.

### Notification token persistence

The project also stores device tokens:

- [src/main/java/com/akashf/springv4/demo/model/UserToken.java](../src/main/java/com/akashf/springv4/demo/model/UserToken.java)
- [src/main/java/com/akashf/springv4/demo/service/UserTokenService.java](../src/main/java/com/akashf/springv4/demo/service/UserTokenService.java)
- [src/main/java/com/akashf/springv4/demo/repository/UserTokenRepo.java](../src/main/java/com/akashf/springv4/demo/repository/UserTokenRepo.java)

This supports token retrieval and reuse, which is useful for browser-generated FCM tokens and frontend notification flow.

---

## 5. Import and Export Functionality

### Supported formats

The project supports:

- CSV
- Excel (`.xlsx`)

Defined by:

- [src/main/java/com/akashf/springv4/demo/enums/ReportType.java](../src/main/java/com/akashf/springv4/demo/enums/ReportType.java)

### Import flow

The actual import implementation is in:

- [src/main/java/com/akashf/springv4/demo/report/users/UserImportExport.java](../src/main/java/com/akashf/springv4/demo/report/users/UserImportExport.java)

#### CSV import

```java
CSVParser parser = CSVFormat.DEFAULT
    .builder()
    .setHeader()
    .setSkipHeaderRecord(true)
    .build()
    .parse(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
```

For each row:

- Create a new `User`
- Fill `name` and `email`
- Save with `repo.save(user)`

#### Excel import

This reads the first sheet and starts from row 1 to skip the header row.

A default CSV template file is included:

- [src/main/resources/report/templates/import-users.csv](../src/main/resources/report/templates/import-users.csv)

### Export flow

The service exports a list of `User` records into:

- Excel workbook via `XSSFWorkbook`
- CSV via `CSVPrinter`

The response uses an attachment header and file type matching the requested format.

### Validation and edge cases

The implementation is simple and practical:

- Missing `file` may lead to runtime exceptions
- Invalid enum values trigger `MethodArgumentTypeMismatchException`
- Validation errors are returned by `GlobalExceptionHandler`
- File parsing errors create a runtime exception and bubbled message in the response

---

## 6. Job Functionality

### Existing jobs

The project includes two job-related patterns:

1. `@Scheduled` cron task
   - [src/main/java/com/akashf/springv4/demo/jobs/cron/TestJob.java](../src/main/java/com/akashf/springv4/demo/jobs/cron/TestJob.java)
2. Async background task executor
   - [src/main/java/com/akashf/springv4/demo/jobs/async/JobWorker.java](../src/main/java/com/akashf/springv4/demo/jobs/async/JobWorker.java)

### Scheduled job

```java
@Scheduled(cron = "0 * * * * *")
public void executeTask() {
    System.out.println("Cron job executed successfully!");
}
```

This means the job executes every second-based minute boundary, i.e., at each minute mark.

### Async job executor

The app enables async processing in:

- [src/main/java/com/akashf/springv4/demo/DemoApplication.java](../src/main/java/com/akashf/springv4/demo/DemoApplication.java)

A custom executor is configured in:

- [src/main/java/com/akashf/springv4/demo/config/AsyncConfig.java](../src/main/java/com/akashf/springv4/demo/config/AsyncConfig.java)

Properties:

- corePoolSize = 5
- maxPoolSize = 10
- queueCapacity = 100

### Job execution flow

1. Client calls `GET /jobs/test/{taskName}`
2. `JobController` invokes `jobWorker.processHeavyJob(taskName)`
3. Method is marked with `@Async("jobExecutor")`
4. The job sleeps for 10 seconds on a background thread
5. The HTTP request returns immediately
6. The background job prints success or is interrupted on failure

### Success and failure handling

The worker does a simulated heavy task using `Thread.sleep(10000)`. There is no DB logging or retry logic; it simply prints to the console.

If the thread is interrupted:

```java
Thread.currentThread().interrupt();
```

This is basic async demonstration code rather than a production workflow system.

---

## 7. PDF Receipt Generation

### Receipt-generation API

The PDF generation functionality is attached to user endpoints:

- `GET /user/{userId}/report`
- `GET /user/{userId}/report2`

These are not generic invoice or receipt APIs; they are user profile PDF reports.

### Input and output

Input:
- `userId` path variable

Output:
- binary PDF bytes
- HTTP headers with `Content-Disposition: attachment; filename=user-profile.pdf`
- content type `application/pdf`

### Raw PDF generation

In `UserService.userRawReportPdf(...)`:

- Gets the user from DB
- Creates a `Document`
- Adds paragraphs like:
  - `User Profile`
  - `Employee Id`
  - `Name`
  - `Email`
- Writes to `ByteArrayOutputStream`

### HTML template PDF generation

In `UserService.userTemplateReportPdf(...)`:

- Creates a `Context`
- Sets the `user` object
- Renders a Thymeleaf template
- Converts the HTML content to PDF with `PdfRendererBuilder`

The template used for this path is:

- [src/main/resources/templates/pdf/temp2.html](../src/main/resources/templates/pdf/temp2.html)

### Example usage

```bash
curl -L -o profile.pdf http://localhost:8080/user/1/report
curl -L -o profile-template.pdf http://localhost:8080/user/1/report2
```

---

## 8. Notification Functionality

### Notification-related APIs

The app includes notification endpoints in `GlobalController`:

- `POST /test-fcm`
- `POST /token/save`
- `GET /token/latest`

There is also a UI page:

- `GET /notify-push`

Controller:

- [src/main/java/com/akashf/springv4/demo/controller/GlobalController.java](../src/main/java/com/akashf/springv4/demo/controller/GlobalController.java)

View:

- [src/main/resources/templates/index.html](../src/main/resources/templates/index.html)

### Notification flow

1. Browser loads `/notify-push`
2. The page calls `/token/latest` to retrieve the last token from the database
3. If no token exists, the user can click “Generate New Token”
4. The browser requests notification permission and generates an FCM token
5. The token is sent to `POST /token/save`
6. The app stores the token in `UserToken`
7. The user can send a notification via `POST /test-fcm`
8. `Helper.notifyFCM(...)` sends a message to Firebase using the FCM token

### Supported notification types

The project does not implement multiple notification channels or templates beyond basic Firebase push messages.

The request object is:

```java
public class NotifyReq {
    private String targetToken;
    private String title;
    private String body;
}
```

This means the app sends a standard push notification with title and body.

### Notification implementation details

`Helper.notifyFCM(NotifyReq req)` builds a `Message` object:

```java
Notification notification = Notification.builder()
    .setTitle(req.getTitle())
    .setBody(req.getBody())
    .build();

Message message = Message.builder()
    .setToken(req.getTargetToken())
    .setNotification(notification)
    .build();
```

Then it calls:

```java
FirebaseMessaging.getInstance().send(message)
```

### Error handling

Failure scenarios:
- invalid or empty token value
- missing Firebase credentials
- FCM send failure
- no token found in database when retrieving `/token/latest`

The controller catches exceptions and wraps them in `Resp.error(...)`.

---

## 9. Spring Boot Testing

### What is actually implemented

The project contains one real test class:

- [src/test/java/com/akashf/springv4/demo/DemoApplicationTests.java](../src/test/java/com/akashf/springv4/demo/DemoApplicationTests.java)

It is a minimal startup test:

```java
@SpringBootTest
class DemoApplicationTests {
    @Test
    void contextLoads() {
    }
}
```

### Current testing status

This project does not yet contain a full suite of:

- controller-layer API tests
- service-layer unit tests
- repository integration tests
- mock-based unit test examples

The codebase is more a feature-learning project than a fully tested enterprise app.

### What the project demonstrates for testing

Although not fully implemented, the structure supports the following test topics:

- `@SpringBootTest` for full application context startup
- `@RestController` endpoints for manual POSTMAN/curl testing
- service logic testing around user creation, PDF generation, import/export
- repository testing for `UserRepo` and `UserTokenRepo`
- validation testing for enums and form data
- negative tests for invalid inputs and empty tokens

### How to execute the test suite

Run:

```bash
./mvnw test
```

or:

```bash
mvn test
```

### Important note

Because the app relies on MySQL, Firebase credentials, and local file configuration, full tests may need the environment to be configured before the app starts successfully.

---

## 10. API Testing Guide

### Basic testing strategy

The project is designed for quick testing from either:

- Postman
- curl
- browser pages
- Swagger-like manual calls (not currently generated)

### Example: create a user

```bash
curl -X POST http://localhost:8080/user \
  -F "name=Akash Fulari" \
  -F "email=akash@example.com" \
  -F "avatar=@C:/path/to/avatar.jpg"
```

### Example: list users

```bash
curl "http://localhost:8080/user/list?page=0&size=10&sort=id,asc"
```

### Example: export CSV

```bash
curl -L -o users.csv http://localhost:8080/user/export/CSV
```

### Example: import CSV

```bash
curl -X POST http://localhost:8080/user/import \
  -F "type=CSV" \
  -F "file=@/path/to/users.csv"
```

### Example: trigger async job

```bash
curl http://localhost:8080/jobs/test/cleanup
```

### Example: save device token

```bash
curl -X POST http://localhost:8080/token/save \
  -H "Content-Type: application/json" \
  -d '{"token":"abc123","device":"WEB"}'
```

### Example: send FCM test notification

```bash
curl -X POST http://localhost:8080/test-fcm \
  -H "Content-Type: application/json" \
  -d '{"targetToken":"DEVICE_TOKEN","title":"Hello","body":"Test push"}'
```

### Common validation and error cases

1. Invalid enum value
   - Example: `/user/export/INVALID`
   - Result: `MethodArgumentTypeMismatchException`

2. Invalid request body
   - Example: JSON body with wrong fields or bad enum value
   - Result: `HttpMessageNotReadableException`

3. Validation failure for user create/update
   - Missing `name` or `email`
   - Result: `MethodArgumentNotValidException`

4. Missing or empty token for `/token/save`
   - Result: `IllegalArgumentException`

5. No token in database for `/token/latest`
   - Result: `No token found`

---

## 11. Code Structure and Design

### Controllers

The main controller classes are:

- [src/main/java/com/akashf/springv4/demo/controller/UserController.java](../src/main/java/com/akashf/springv4/demo/controller/UserController.java)
- [src/main/java/com/akashf/springv4/demo/controller/GlobalController.java](../src/main/java/com/akashf/springv4/demo/controller/GlobalController.java)
- [src/main/java/com/akashf/springv4/demo/controller/JobController.java](../src/main/java/com/akashf/springv4/demo/controller/JobController.java)
- [src/main/java/com/akashf/springv4/demo/controller/WebViewController.java](../src/main/java/com/akashf/springv4/demo/controller/WebViewController.java)
- [src/main/java/com/akashf/springv4/demo/controller/HelloController.java](../src/main/java/com/akashf/springv4/demo/controller/HelloController.java)

These classes are responsible for HTTP routing and orchestration.

### Services

The business logic classes include:

- [src/main/java/com/akashf/springv4/demo/service/UserService.java](../src/main/java/com/akashf/springv4/demo/service/UserService.java)
- [src/main/java/com/akashf/springv4/demo/service/UserTokenService.java](../src/main/java/com/akashf/springv4/demo/service/UserTokenService.java)
- [src/main/java/com/akashf/springv4/demo/service/Helper.java](../src/main/java/com/akashf/springv4/demo/service/Helper.java)
- [src/main/java/com/akashf/springv4/demo/service/Resp.java](../src/main/java/com/akashf/springv4/demo/service/Resp.java)

### Repositories

- [src/main/java/com/akashf/springv4/demo/repository/UserRepo.java](../src/main/java/com/akashf/springv4/demo/repository/UserRepo.java)
- [src/main/java/com/akashf/springv4/demo/repository/UserTokenRepo.java](../src/main/java/com/akashf/springv4/demo/repository/UserTokenRepo.java)

These repositories extend `JpaRepository` and are the access point for JPA entity persistence.

### Models and DTOs

Entities:

- [src/main/java/com/akashf/springv4/demo/model/User.java](../src/main/java/com/akashf/springv4/demo/model/User.java)
- [src/main/java/com/akashf/springv4/demo/model/UserToken.java](../src/main/java/com/akashf/springv4/demo/model/UserToken.java)

DTOs:

- [src/main/java/com/akashf/springv4/demo/dto/SetUserReq.java](../src/main/java/com/akashf/springv4/demo/dto/SetUserReq.java)
- [src/main/java/com/akashf/springv4/demo/dto/PaginReq.java](../src/main/java/com/akashf/springv4/demo/dto/PaginReq.java)
- [src/main/java/com/akashf/springv4/demo/dto/ImportUserReq.java](../src/main/java/com/akashf/springv4/demo/dto/ImportUserReq.java)
- [src/main/java/com/akashf/springv4/demo/dto/ExportUserReq.java](../src/main/java/com/akashf/springv4/demo/dto/ExportUserReq.java)
- [src/main/java/com/akashf/springv4/demo/dto/NotifyReq.java](../src/main/java/com/akashf/springv4/demo/dto/NotifyReq.java)
- [src/main/java/com/akashf/springv4/demo/dto/SaveTokenReq.java](../src/main/java/com/akashf/springv4/demo/dto/SaveTokenReq.java)
- [src/main/java/com/akashf/springv4/demo/dto/TokenResp.java](../src/main/java/com/akashf/springv4/demo/dto/TokenResp.java)

### Exception handling

The project has a global exception handler:

- [src/main/java/com/akashf/springv4/demo/exception/GlobalExceptionHandler.java](../src/main/java/com/akashf/springv4/demo/exception/GlobalExceptionHandler.java)

It handles:

- validation errors
- enum type mismatch
- invalid JSON body

### Utilities and configuration

Other important classes:

- [src/main/java/com/akashf/springv4/demo/config/AsyncConfig.java](../src/main/java/com/akashf/springv4/demo/config/AsyncConfig.java)
- [src/main/java/com/akashf/springv4/demo/config/FCMConfig.java](../src/main/java/com/akashf/springv4/demo/config/FCMConfig.java)
- [src/main/java/com/akashf/springv4/demo/config/FileConfig.java](../src/main/java/com/akashf/springv4/demo/config/FileConfig.java)
- [src/main/java/com/akashf/springv4/demo/config/LocaleConfig.java](../src/main/java/com/akashf/springv4/demo/config/LocaleConfig.java)
- [src/main/java/com/akashf/springv4/demo/filters/LoggingFilter.java](../src/main/java/com/akashf/springv4/demo/filters/LoggingFilter.java)
- [src/main/java/com/akashf/springv4/demo/config/interceptors/WebInterceptor.java](../src/main/java/com/akashf/springv4/demo/config/interceptors/WebInterceptor.java)

### How the layers interact

The pattern is straightforward and beginner-friendly:

- Controller receives HTTP request
- DTO carries JSON or multipart data
- Service reads/writes database records and generates files
- Repository persists JPA entities
- Response object is returned to the client

This makes the project good for understanding the flow from request to database and back.

---

## 12. End-to-End Workflows

### Workflow 1: Create a user and fetch it back

1. Send a `POST /user` call with multipart form data
2. The controller receives `SetUserReq`
3. `UserService.createUser(...)` validates and saves the entity
4. The repository persists the row in MySQL
5. `GET /user/{userId}` returns the user record
6. The result is returned wrapped in `ApiResp`

### Workflow 2: Import CSV and export Excel

1. Client sends file to `POST /user/import`
2. Import logic reads CSV or XLSX rows
3. Each row becomes a `User` entity
4. Data is saved through JPA
5. Client later calls `GET /user/export/EXCEL`
6. The service fetches all users and builds an Excel workbook
7. Browser downloads the file

### Workflow 3: Generate a user PDF report

1. Client hits `GET /user/{userId}/report`
2. Service fetches user from DB
3. PDF generation creates a simple profile document
4. Response includes attachment headers
5. Browser downloads the PDF file

### Workflow 4: Async job submission

1. Client calls `GET /jobs/test/{taskName}`
2. `JobWorker` is invoked asynchronously
3. The method runs on a background executor thread
4. The client receives a response immediately
5. Console output marks completion after the sleep period

### Workflow 5: Notification token and push message cycle

1. Frontend loads `/notify-push`
2. Browser generates an FCM token
3. The token is saved to `UserToken` using `POST /token/save`
4. On refresh, `/token/latest` retrieves it
5. User triggers `POST /test-fcm`
6. Firebase pushes the message to the target device

---

## 13. Troubleshooting and Common Issues

### 1. MySQL connection errors

Symptom:
- Application fails to start
- JDBC connection refused or database not found

Check:
- MySQL is running
- Username/password matches application config
- Database `first_spring` exists or is auto-created

### 2. Firebase initialization errors

Symptom:
- Notification endpoints fail
- `Failed to initialize Firebase Admin SDK`

Check:
- `firebase-service-account.json` exists in resources
- `fcm.notify=true` is set
- Credentials are valid

### 3. File upload problems

Symptom:
- Multipart upload fails or files are not written

Check:
- `spring.servlet.multipart` settings are present
- Directory `asstes/users/profile/` exists and is writable
- For local mode, `storage.local.path` is valid

### 4. Invalid enum values

Symptom:
- Request path or body contains unsupported values

Example:
- `/user/export/XYZ`
- JSON contains `"type":"UNKNOWN"`

The project returns a 400 error via `GlobalExceptionHandler`.

### 5. Token not found

Symptom:
- `GET /token/latest` returns `No token found`

This is expected on the first load before any token has been generated and saved.

### 6. PDF generation errors

Symptom:
- user report fails or generates an invalid file

Check:
- User id exists in DB
- Template path is valid
- Thymeleaf template is not corrupted

### 7. Job does not appear to run

This is a background async example. The request returns immediately, and the work happens in a thread pool. Check console logs and executor configuration.

---

## 14. Quick Reference

### Main endpoints summary

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/hello` | Localized greeting |
| GET | `/download-template/{template}/{type}` | Download template CSV/XLSX |
| POST | `/test-fcm` | Send Firebase notification |
| POST | `/token/save` | Save FCM token |
| GET | `/token/latest` | Get latest FCM token |
| GET | `/notify-push` | Browser-based notification test panel |
| GET | `/jobs/test/{taskName}` | Trigger async background task |
| POST | `/user` | Create a user |
| PUT | `/user/{userId}` | Update a user |
| POST | `/user/list` | List users with pagination body |
| GET | `/user/list` | List users with query-pageable |
| GET | `/user/{userId}` | Get a single user |
| DELETE | `/user/{userId}` | Delete a user |
| GET | `/user/{userId}/report` | Generate raw PDF |
| GET | `/user/{userId}/report2` | Generate template PDF |
| GET | `/user/export/{type}` | Export users as file |
| POST | `/user/export` | Export users with body config |
| POST | `/user/import` | Import CSV/XLSX users |

### Important classes

- `DemoApplication`
- `UserController`
- `GlobalController`
- `JobController`
- `UserService`
- `UserImportExport`
- `UserTokenService`
- `UserRepo`
- `UserTokenRepo`
- `Helper`
- `Resp`
- `GlobalExceptionHandler`

### Important test class

- `DemoApplicationTests`

### Common commands

```bash
./mvnw clean install
./mvnw spring-boot:run
./mvnw test
```

### Common testing patterns

- Use curl for quick endpoint checks
- Use Postman for multipart uploads and custom JSON bodies
- Use the browser UI at `/notify-push` for Firebase flow testing
- Use the `actuator/scheduledtasks` endpoint for checking scheduled jobs

---

## 15. Future Extensions

This project is a good learning foundation, but the current implementation is deliberately simple. Suggested future extensions include:

### Suggested testing additions

- Controller tests with MockMvc
- Service tests with JUnit 5 and Mockito
- Repository tests with H2 in-memory database
- Validation tests for blank input, invalid emails, and invalid enum values
- File import/export tests for CSV and Excel edge cases

### Suggested Spring Boot features to add

- OpenAPI / Swagger documentation
- Role-based security with JWT or OAuth2
- Transaction support for complex business flows
- Database migrations with Flyway or Liquibase
- Better error response contracts
- Background job persistence with database tracking
- Notification templates and multi-device support
- File storage abstraction with dedicated service contracts and upload metadata

### Important distinction

The features above are future suggestions only. They are not part of the current project implementation.

This project today is best understood as a lightweight Spring Boot reference app for learning REST APIs, file handling, jobs, notifications, and testing fundamentals.

---

## Final Summary

This project demonstrates a compact but useful Spring Boot learning stack:

- REST APIs
- JPA persistence
- Multipart uploads
- CSV and Excel import/export
- PDF generation
- Async jobs and scheduled tasks
- Firebase push notification basics
- Localized messages
- Minimal Spring Boot test startup validation

If you are learning backend API development, this project is a very practical reference to understand how the layer structure and common features fit together in a real Spring Boot app.
