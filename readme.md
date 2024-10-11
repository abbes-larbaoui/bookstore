
# Bookstore Application

This is a simple Spring Boot application for managing a bookstore, providing APIs for authentication, public book access, and CRUD operations for authenticated users.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [Docker Compose for PostgreSQL](#docker-compose-for-postgresql)
- [API Endpoints](#api-endpoints)
    - [Login API](#login-api)
    - [Public APIs](#public-apis)
    - [Authenticated APIs](#authenticated-apis)
- [Error Handling](#error-handling)
- [Additional Information](#additional-information)

## Technologies Used
- Java 17
- Spring Boot 3.3.4
- Maven
- Spring Security
- JWT for Authentication
- Pagination using `Pageable`
- Multipart File Handling for images
- PostgreSQL Database

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/abbes-larbaoui/bookstore.git
   cd bookstore
   ```

2. **Start the database:**
    ```bash
    docker-compose up
    ```
   
3. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Swagger Documentation:**
   After starting the application, you can access the Swagger UI for API documentation at http://localhost:8080/swagger-ui/index.html`.


## API Endpoints

### Login API

#### **Login User**
- **POST** `/api/auth/login`
- **Request Body**:
  ```json
  {
      "username": "user1",
      "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
      "accessToken": "jwt_token_here"
  }
  ```

### Public APIs

These APIs are accessible without authentication.

#### **Get All Books (Public)**
- **GET** `/api/v1/books`
- **Parameters**: `page` (Optional), `size` (Optional), `keyword` (Optional)
- **Response**:
    - Returns a paginated list of all books with search functionality.

#### **Get Book by ID (Public)**
- **GET** `/api/v1/books/{id}`
- **Path Variable**: `id` (Long)
- **Response**:
    - Returns the details of a book by its ID.

### Authenticated APIs

These APIs require the user to be authenticated and have a valid JWT token.

#### **Get All Books (User)**
- **GET** `/api/v1/books`
- **Parameters**: `page` (Optional), `size` (Optional), `keyword` (Optional)
- **Response**:
    - Returns a paginated list of books based on the user's input.

#### **Get Book by ID (User)**
- **GET** `/api/v1/books/{id}`
- **Path Variable**: `id` (Long)
- **Response**:
    - Returns the details of a book by its ID, restricted to the authenticated user.

#### **Add a New Book**
- **POST** `/api/v1/books`
- **Request Params**: `file` (Multipart), `title`, `description`, `price`
- **Response**:
    - Adds a new book to the bookstore.

#### **Update a Book**
- **PUT** `/api/v1/books/{id}`
- **Request Body**:
  ```json
  {
      "title": "New Title",
      "description": "Updated Description",
      "price": 15.99
  }
  ```
- **Response**:
    - Updates an existing book.

#### **Update Book Cover Image**
- **PUT** `/api/v1/books/{id}/update-cover-image`
- **Request Params**: `file` (Multipart)
- **Response**:
    - Updates the cover image of a book.

#### **Delete a Book**
- **DELETE** `/api/v1/books/{id}`
- **Response**:
    - Deletes a book by its ID.

## Error Handling
The application handles different types of exceptions:
- **404 (Not Found)**: When a book is not found.
- **403 (Forbidden)**: When the user is unauthorized to perform the action.
- **406 (Not Acceptable)**: For invalid inputs.
- **500 (Internal Server Error)**: For unexpected server errors.

## Additional Information
- **Security**: The application uses JWT for secure authentication.
- **Pagination**: APIs support pagination using `page` and `size` query parameters.
- **Search**: You can search books using the `keyword` parameter in the `GET` APIs.

