# Online Quiz Application

A web-based quiz platform designed for user participation in quizzes and administrative management of questions, users, and results.

**Tech Stack**: Java 17, Spring Boot, JSP, MySQL, Maven  
**Architecture**: Layered architecture with separation of concerns (Controller, Service, DAO, Domain).

---

## Project Overview

The Online Quiz Application includes the following key features:
- **User Management**: Registration, login, and profile management.
- **Quiz Participation**: Users can participate in quizzes and view results.
- **Administrative Control**: Admins can manage users, categories, questions, and quiz results.
- **Question CRUD Operations**: Create, edit, delete, and paginate quiz questions.
- **Contact Form**: Users can submit queries to the admin.

---

## Project Structure

### **Backend**
- **Config**: Configuration classes for authentication filters and Spring MVC settings.
- **Controller**: Handles incoming HTTP requests and routes them to the appropriate service.
- **DAO (Data Access Object)**: Interfaces with the database to perform CRUD operations.
- **Domain (Model)**: Contains core data entities representing users, quizzes, questions, and choices.
- **Service**: Contains the business logic for user management, quiz handling, and question management.
- **Filter**: Implements filters for user authentication and session validation.

### **Frontend (JSP Views)**
- JSP files located in `webapp/WEB-INF/jsp/` for rendering the user interface:
  - **`home.jsp`**: Displays the home page.
  - **`login.jsp`**: User login form.
  - **`register.jsp`**: User registration form.
  - **`quiz_page.jsp`**: Quiz participation page.
  - **`quiz_result.jsp`**: Displays quiz results.
  - **Admin JSPs**: Views for admin tasks such as `admin_question_management.jsp`, `admin_user_management.jsp`, etc.

---

## Detailed Package Descriptions

### **1. Config**
- **`FilterConfig`**: Defines filters for session management and authorization.
- **`WebMvcConfig`**: Configures resource handlers, view resolvers, and other Spring MVC settings.

### **2. Controller**
- **AdminController**: Handles admin functions such as managing users, questions, and quiz results.
- **UserController**: Manages user registration, login, and logout.
- **QuizController**: Handles quiz-related actions (starting quizzes, submitting answers).
- **ContactController**: Handles contact form submissions and admin responses.
- **HomeController**: Manages navigation for the home page.

### **3. DAO (Data Access Object)**
- Interfaces for database operations:
  - **CategoryDao**: Fetches and manages quiz categories.
  - **QuestionDao**: Handles question-related queries, including CRUD and pagination.
  - **ChoiceDao**: Manages quiz answer choices.
  - **QuizDao & QuizQuestionDao**: Manage quiz data and mapping between quizzes and questions.
  - **UserDao**: Handles user data access.
  - **ContactDao**: Stores and retrieves contact form messages.

### **4. Domain (Model)**
- Core entity classes:
  - **User**: Represents users (regular or admin).
  - **Question & Choice**: Represent quiz questions and possible answers.
  - **Quiz & QuizQuestion**: Represent a quiz and its questions.
  - **QuizResultView**: Represents summarized quiz result data for display.
  - **Contact**: Stores contact form information.

### **5. Service**
- Implements business logic:
  - **UserService**: Manages user registration, authentication, and updates.
  - **QuestionService**: Handles question creation, editing, deletion, and pagination.
  - **QuizService**: Manages quiz sessions, stores results, and calculates scores.
  - **CategoryService**: Manages quiz categories.
  - **ContactService**: Handles contact form submissions and retrieval.

---

## Key Functionalities
- **User Features**:
  - Register, log in, and log out.
  - Participate in quizzes and view detailed quiz results.
- **Admin Features**:
  - Manage categories, questions, users, and quiz results.
  - Enable or disable user accounts.
  - Add, edit, or delete quiz questions.

---

## Installation and Setup

### **Prerequisites**
- Java 17+
- MySQL Database
- Maven

### **Steps**
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd online-quiz-app
   ```
3. Configure database settings in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/quiz_app
   spring.datasource.username=root
   spring.datasource.password=password
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access the application at `http://localhost:8080`.

---

## Example User Flows

### **User Flow**:
- Visit the home page and register or log in.
- Select a quiz and answer questions.
- View quiz results.

### **Admin Flow**:
- Log in as an admin.
- Manage questions, users, and quiz results through the admin dashboard.

---

## Contributions
We welcome contributions! To contribute:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-branch-name`).
3. Commit your changes (`git commit -m 'Add feature'`).
4. Push to your branch (`git push origin feature-branch-name`).
5. Open a pull request.

---

Thank you for using the Online Quiz Application! If you encounter any issues or have questions, feel free to open an issue or contact the maintainers.

