# Cloudage - Cloud Image Storage & Management

A Spring Boot application that provides secure cloud-based image storage with AWS S3 integration, user authentication, and image format conversion capabilities.

## Features

- 🔐 **JWT Authentication** - Secure user registration, login, and session management
- ☁️ **AWS S3 Storage** - Reliable cloud storage with presigned URL uploads
- 🖼️ **Image Format Support** - JPEG, PNG, WebP, GIF, BMP, and TIFF formats
- 👤 **User Management** - Profile management, password updates, account deactivation
- 🔄 **Async Processing** - AWS SQS integration for background tasks
- 📧 **Email Integration** - Account activation and notifications
- 🛡️ **Security** - Spring Security with role-based access control

## Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Java Version**: 21
- **Database**: PostgreSQL
- **Cloud Storage**: AWS S3
- **Message Queue**: AWS SQS
- **Authentication**: JWT with Spring Security
- **Build Tool**: Maven
- **Image Processing**: TwelveMonkeys ImageIO, WebP support

## Project Structure

```
src/main/java/com/keshan.cloudage.org/
├── auth/              # Authentication services and DTOs
├── config/            # Security and application configuration
├── controller/        # REST API controllers
│   ├── AuthController.java     # Authentication endpoints
│   ├── UserController.java     # User management
│   └── S3Controller.java       # Image upload/download
├── dto/               # Data Transfer Objects
├── jwt/               # JWT token management
├── mail/              # Email service integration
├── model/             # JPA entities and enums
├── repository/        # Data access layer
├── service/           # Business logic
└── util/              # Utility classes
```

## Prerequisites

- Java 21
- Maven 3.8+
- PostgreSQL database
- AWS account with S3 and SQS access

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/Dulaksha-Keshan/Cloudage.git
cd Cloudage
```

### 2. Configure application properties

Create `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cloudage
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  mail:
    host: ${MAIL_HOST}
    port: ${MAIL_PORT}
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}

# AWS Configuration
cloud:
  aws:
    credentials:
      access-key: ${AWS_ACCESS_KEY}
      secret-key: ${AWS_SECRET_KEY}
    region:
      static: ${AWS_REGION:us-east-1}
    s3:
      bucket: ${S3_BUCKET_NAME}

#jwt public and private keys are under the resources for LOCAL TESTING ONLY
src/main/java/resources/keys.loacl-only
├── private_key.pem/             # private key
├── public_key.pem/              # public key
```

### 3. Set up environment variables

```bash
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export AWS_ACCESS_KEY=your_aws_access_key
export AWS_SECRET_KEY=your_aws_secret_key
export AWS_REGION=us-east-1
export S3_BUCKET_NAME=your-s3-bucket
export JWT_SECRET=your-256-bit-secret-key
export MAIL_HOST=your-smtp-host
export MAIL_PORT=587
export MAIL_USERNAME=your-email
export MAIL_PASSWORD=your-email-password
```

### 4. Run the application

```bash
# Development mode
./mvnw clean install #to install dependencies 

./mvnw spring-boot:run

# Or build and run
mvn clean package
java -jar target/cloudage.org-0.0.1-SNAPSHOT.war
```

The application will start on `http://localhost:8090`

## API Documentation

### Authentication Endpoints

#### Register a new user
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123"
}
```

#### Refresh Token
```http
POST /api/auth/refresh
Cookie: refreshToken=your_refresh_token
```

### User Management

#### Get user profile
```http
GET /api/users/me
Authorization: Bearer {jwt_token}
```

#### Update profile
```http
POST /api/users/me
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith"
}
```

#### Change password
```http
POST /api/users/me/password
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "currentPassword": "oldPassword",
  "newPassword": "newSecurePassword",
  "confirmPassword" : "newConfirmPassword"
}
```

### Image Storage

#### Get presigned upload URL
```http
GET /api/s3/upload-req/{fileName}?type=image/jpeg&size=1024000
Authorization: Bearer {jwt_token}
```

Response:
```json
"https://your-bucket.s3.amazonaws.com/images/uuid_filename.jpg?presigned_params"
```

#### Get user's images
```http
GET /api/s3/get-images
Authorization: Bearer {jwt_token}
```

Response:
```json
{
  "image1_key": "https://cloudfront-url/image1.jpg",
  "image2_key": "https://cloudfront-url/image2.jpg"
}
```

#### Delete image
```http
DELETE /api/s3/delete?s3key=images/uuid_filename.jpg
Authorization: Bearer {jwt_token}
```

## Supported Image Formats

- **JPEG** (.jpg, .jpeg)
- **PNG** (.png)
- **WebP** (.webp)
- **GIF** (.gif)


## Security Features

- JWT-based stateless authentication
- Password encryption with BCrypt
- CORS configuration for frontend integration
- Request validation
- Secure file upload with type validation
- Presigned URLs for direct S3 uploads

## Development Setup

### Database Setup
```sql
-- Create database
CREATE DATABASE cloudage;

-- Grant privileges (adjust username as needed)
GRANT ALL PRIVILEGES ON DATABASE cloudage TO your_user;
```

### AWS S3 Bucket Setup
1. Create an S3 bucket
2. Configure bucket CORS policy:
```json
[
  {
    "AllowedHeaders": ["*"],
    "AllowedMethods": ["GET", "PUT", "POST", "DELETE"],
    "AllowedOrigins": ["http://localhost:5173"],
    "ExposeHeaders": []
  }
]
```

### Frontend Integration Example

```javascript
// Get upload URL
const getUploadUrl = async (file) => {
  const response = await fetch(
    `/api/s3/upload-req/${file.name}?type=${file.type}&size=${file.size}`,
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }
  );
  return response.text();
};

// Upload file directly to S3
const uploadFile = async (file) => {
  const uploadUrl = await getUploadUrl(file);
  
  await fetch(uploadUrl, {
    method: 'PUT',
    body: file,
    headers: {
      'Content-Type': file.type
    }
  });
};
```

## Deployment

### Maven Profiles

The project includes two profiles:

- **embedded-tomcat** (default): For local development
- **external-tomcat**: For production deployment

```bash
# Build for external Tomcat
mvn clean package -Pexternal-tomcat
```



## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request


## Author

**Dulaksha Keshan**
- GitHub: [@Dulaksha-Keshan](https://github.com/Dulaksha-Keshan)

---

*A cloud image storage solution built with Spring Boot and AWS*
