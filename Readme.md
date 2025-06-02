# Library Management System

A robust Java-based Library Management System that helps librarians and members manage books, transactions, and memberships effectively.

## Diagrams

### Class Diagram
![Class Diagram](diagrams/ClassDiagramUML.png)

### Database Model
![Database Diagram](diagrams/DatabaseER.png)


## Features

### Authentication
- Separate login systems for librarians and members
- Secure password-based authentication
- Role-based access control

### Librarian Portal:
* **Authentication:** Secure login for admin users.
* **Book Management:**
   * Add new books with title, author, genre, and quantity.
   * Update the stock (available copies) of existing books.
   * "Delete" books (marks them as unavailable and sets copies to zero; a soft delete).
   * List all books in the library.
   * List only currently available books.
* **Member Management:**
   * Add new members with name, username, and password.
   * "Delete" members (marks them as inactive if they have no active transactions).
   * View all registered members.
   * View only active members.
   * Find members by their ID or username.
   * Re-activate inactive members.
* **Transaction Management:**
   * View all transactions (borrows and returns).
   * View transactions for a specific member.
   * Filter transactions by type (BORROW, RETURN).
   * Filter transactions by status (ACTIVE, COMPLETED) and type.
   * Find specific transactions by their ID.
   * View all transactions related to a specific book ID.

### Member Portal:
* **Authentication:** Secure login for registered and active members.
* **Book Borrowing:**
   * View available books.
   * Borrow an available book.
* **Book Returning:**
   * View currently borrowed books (active borrow transactions).
   * Return a borrowed book.
* **Transaction History:**
   * View all personal transaction history.

### Transaction System
- Automatic due date calculation
- Late return detection
- Status tracking (ACTIVE/COMPLETED)
- Transaction history maintenance

## Technologies Used

* **Java:** Core programming language.
* **JPA (Java Persistence API):** For Object-Relational Mapping (ORM) and database interaction (inferred from `javax.persistence` imports).
* **SQL Database:** (Not specified, but typical for JPA usage, e.g., H2, MySQL, PostgreSQL).

##  Project Structure (Core Components)

* **`org.lms.controllers`**:
   * `LibrarianController.java`: Handles logic and user interface for librarian actions.
   * `MembersController.java`: Handles logic and user interface for member actions.
* **`org.lms.dao`**: Data Access Objects for database operations.
   * `BookDAO.java`: Manages CRUD operations for Books.
   * `MemberDAO.java`: Manages CRUD operations for Members.
   * `TransactionDAO.java`: Manages CRUD operations for Transactions.
* **`org.lms.model`**: JPA Entity classes representing database tables.
   * `Book.java`: Represents a book.
   * `Member.java`: Represents a library member.
   * `Transaction.java`: Represents a book borrowing or returning transaction.
* **`org.lms.enums`**: Enumerations used in the project.
   * `Status.java`: Defines various statuses (e.g., ACTIVE, INACTIVE, AVAILABLE, COMPLETED).
   * `TransactionType.java`: Defines transaction types (BORROW, RETURN).
* **`org.lms.service`**: Service layer classes (e.g., `AuthService`, `BookService`) that encapsulate business logic and interact with DAOs. (Note: Service class code was not provided but their usage is seen in controllers).

### Dependencies
- Hibernate Core 5.6.15.Final
- PostgreSQL Driver 42.6.0
- JPA API 2.2



### How To Run
1.  **Prerequisites:**
   * Java Development Kit (JDK) installed.
   * Maven or Gradle (if used for dependency management, not specified).
   * A configured SQL database (if not using an in-memory one like H2 configured via `persistence.xml`).
2.  **Setup:**
   * Ensure `persistence.xml` is correctly configured with database connection details in the `META-INF` directory (typically `src/main/resources/META-INF/persistence.xml`).
      * Create a PostgreSQL database
      * Configure database connection in `persistence.xml`:
        - Database URL `<property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/<DATABASE_NAME>"/>`
            - Username
            - Password
      * Compile the Java code.
3.  **Execution:**
   * Run the main class that initializes the application and presents the initial login prompt.

## Database Schema

The system uses the following main entities:
- Books (storing book information)
- Members (storing member details)
- Transactions (tracking borrows and returns)

## Security Features

- Password-protected access
- Role-based authorization
- Session management
- Input validation


## Documentation

For detailed usage, setup, and architecture, refer to the full documentation below:

* [User And Deployment Guide (PDF)](instructions\UserAndDeploymentGuide.pdf)
* [Function Explaination (PDF)](instructions\FunctionExplaination.pdf)



[GitHub Profile](https://github.com/yourusername)


