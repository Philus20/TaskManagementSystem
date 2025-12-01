# UML Diagram Documentation - Task Management System

## Class Structure Overview

This document provides complete class, attribute, and method information for creating UML diagrams.

---

## 📦 Models Package

### 1. User (Abstract Class)

**Package:** `models`  
**Type:** Abstract Class

**Attributes:**

- `protected String id`
- `protected String name`
- `protected String email`
- `protected String role`
- `private static int idCounter` (class variable)

**Methods:**

- `+ User(String name, String email, String role)` (constructor)
- `+ String getId()`
- `+ String getName()`
- `+ String getEmail()`
- `+ String getRole()`
- `+ abstract void displayRole()`
- `+ String toString()`

**Relationships:**

- Parent of `RegularUser` and `AdminUser`

---

### 2. RegularUser (Concrete Class)

**Package:** `models`  
**Type:** Concrete Class  
**Extends:** `User`

**Attributes:**

- (Inherits all from User)

**Methods:**

- `+ RegularUser(String name, String email)` (constructor)
- `+ void displayRole()` (override)

**Relationships:**

- Extends `User`

---

### 3. AdminUser (Concrete Class)

**Package:** `models`  
**Type:** Concrete Class  
**Extends:** `User`

**Attributes:**

- (Inherits all from User)

**Methods:**

- `+ AdminUser(String name, String email)` (constructor)
- `+ void displayRole()` (override)

**Relationships:**

- Extends `User`

---

### 4. Project (Abstract Class)

**Package:** `models`  
**Type:** Abstract Class

**Attributes:**

- `public String id`
- `public String name`
- `public String description`
- `public String type`
- `public int teamSize`
- `public double budget`
- `public List<String> assignedUserIds`
- `private static int idCounter` (class variable)

**Methods:**

- `+ Project(String name, String description, String type, int teamSize, double budget)` (constructor)
- `+ String getId()`
- `+ String getType()`
- `+ double getBudget()`
- `+ abstract void displayAttributes()`
- `+ void assignUser(String userId)`
- `+ void removeUser(String userId)`
- `+ List<String> getAssignedUserIds()`

**Relationships:**

- Parent of `SoftwareProject` and `HardwareProject`
- Contains `List<String>` for assignedUserIds

---

### 5. SoftwareProject (Concrete Class)

**Package:** `models`  
**Type:** Concrete Class  
**Extends:** `Project`

**Attributes:**

- `private String programmingLanguage`
- (Inherits all from Project)

**Methods:**

- `+ SoftwareProject(String name, String description, String type, int teamSize, String programmingLanguage, double budget)` (constructor)
- `+ String getProgrammingLanguage()`
- `+ void displayAttributes()` (override)

**Relationships:**

- Extends `Project`

---

### 6. HardwareProject (Concrete Class)

**Package:** `models`  
**Type:** Concrete Class  
**Extends:** `Project`

**Attributes:**

- `private String hardwareType`
- (Inherits all from Project)

**Methods:**

- `+ HardwareProject(String name, String description, String type, int teamSize, String hardwareType, double budget)` (constructor)
- `+ String getHardwareType()`
- `+ void displayAttributes()` (override)

**Relationships:**

- Extends `Project`

---

### 7. Task (Concrete Class)

**Package:** `models`  
**Type:** Concrete Class

**Attributes:**

- `private String taskName`
- `private String taskId`
- `private String taskStatus`
- `private String projectId`
- `private String assignedUserId`
- `private static int idCounter` (class variable)

**Methods:**

- `+ Task(String taskName, String taskStatus, String projectId)` (constructor)
- `+ Task(String taskName, String taskStatus, String projectId, String assignedUserId)` (constructor)
- `+ void setTaskStatus(String status)`
- `+ String getTaskName()`
- `+ String getTaskId()`
- `+ String getTaskStatus()`
- `+ String getProjectId()`
- `+ String getAssignedUserId()`
- `+ void setAssignedUserId(String assignedUserId)`

**Relationships:**

- References `Project` via `projectId`
- References `User` via `assignedUserId`

---

### 8. ProjectStatusReportDto (Data Transfer Object)

**Package:** `models`  
**Type:** Concrete Class

**Attributes:**

- `public String projectId`
- `public String projectName`
- `public int Tasks`
- `public int Completed`

**Methods:**

- `+ ProjectStatusReportDto(String projectId, String projectName, int Tasks, int Completed)` (constructor)

**Relationships:**

- Used by `ReportService` and `ProjectService`

---

## 🔧 Services Package

### 9. UserService (Concrete Class)

**Package:** `services`  
**Type:** Concrete Class

**Attributes:**

- `private List<User> users`
- `private User currentUser`

**Methods:**

- `+ UserService()` (constructor)
- `+ User createRegularUser(String name, String email)`
- `+ User createAdminUser(String name, String email)`
- `+ User login(String userId)`
- `+ void logout()`
- `+ User getCurrentUser()`
- `+ List<User> getAllUsers()`
- `+ User getUserById(String userId)`
- `+ void displayCurrentUser()`

**Relationships:**

- Manages `User` objects (RegularUser, AdminUser)
- Used by `ConsoleMenu`

---

### 10. ProjectService (Concrete Class)

**Package:** `services`  
**Type:** Concrete Class

**Attributes:**

- `public List<Project> projectsRepository`
- `TaskService taskService` (composition)

**Methods:**

- `+ ProjectService()` (constructor)
- `+ void addProject(Project project)`
- `+ List<Project> getAllProjects()`
- `+ String filterProjectByType(String type)`
- `+ void displayAllProjects()`
- `+ String searchByBudgetRange(double min, double max)`
- `+ Project getProjectById(String id)`
- `+ List<ProjectStatusReportDto> getProjectStatusReport(TaskService sourceTaskService)`
- `+ double calculateAverageProjectStatusReport(TaskService sourceTaskService)`
- `+ boolean assignUserToProject(String projectId, String userId)`
- `+ boolean removeUserFromProject(String projectId, String userId)`

**Relationships:**

- Manages `Project` objects (SoftwareProject, HardwareProject)
- Uses `TaskService` for reports
- Uses `ConsoleMenu` for display
- Returns `ProjectStatusReportDto`

---

### 11. TaskService (Concrete Class)

**Package:** `services`  
**Type:** Concrete Class

**Attributes:**

- `private final List<Task> tasks`

**Methods:**

- `+ TaskService()` (constructor)
- `+ void addTask(Task task)`
- `+ List<Task> getAllTasks()`
- `+ Task getTaskById(String taskId)`
- `+ Task updateTaskStatus(String taskId, String taskStatus)`
- `+ void deleteTask(String taskId)`
- `+ List<Task> getTasksByProjectId(String projectId)`
- `+ double calculateCompletionRate(String projectId)`

**Relationships:**

- Manages `Task` objects
- Used by `ProjectService` and `ReportService`

---

### 12. ReportService (Concrete Class)

**Package:** `services`  
**Type:** Concrete Class

**Attributes:**

- `private final ProjectService projectService`
- `private final TaskService taskService`

**Methods:**

- `+ ReportService(ProjectService projectService, TaskService taskService)` (constructor)
- `+ List<ProjectStatusReportDto> getProjectStatusReport()`
- `+ double calculateAverageProjectStatusReport()`

**Relationships:**

- Uses `ProjectService` and `TaskService`
- Returns `ProjectStatusReportDto`
- Used by `ConsoleMenu`

---

## 🎨 Utils Package

### 13. ConsoleMenu (Concrete Class)

**Package:** `utils`  
**Type:** Concrete Class

**Attributes:**

- `private static ProjectService projectService`
- `private static TaskService taskService`
- `private static ReportService reportService`
- `private static UserService userService`
- `static Scanner scanner`

**Methods (Public Static):**

- `+ static void setProjectService(ProjectService service)`
- `+ static void setTaskService(TaskService service)`
- `+ static void setReportService(ReportService service)`
- `+ static void setUserService(UserService service)`
- `+ static String createProjectInteractive()`
- `+ static void printingTitle(String title)`
- `+ static void mainMenu()`
- `+ static void returnToMain()`
- `+ static void returnToTesting()`
- `+ static void projectCatalog()`
- `+ static void displayProjects(List<Project> projects)`
- `+ static void displayProjects(List<Project> projects, String projectType)`
- `+ static void displayProjectTaks(List<Task> tasks)`
- `+ static void displayProjectTaks(List<Task> tasks, double completionRate)`
- `+ static void displayProjectDetails(String id)`
- `+ static void addingNewTaskMenu()`
- `+ static void updatingTask()`
- `+ static void removingTask()`
- `+ static void initialLoginMenu()`
- `+ static void signUpMenu()`
- `+ static void createUserMenu()`
- `+ static void switchUserMenu()`
- `+ static void loginUserMenu()`
- `+ static void displayAllUsers()`
- `+ static void displayCurrentUser()`
- `+ static void assignUserToProjectMenu()`
- `+ static void enterProjectId()`
- `+ static void taskMainMenu()`
- `+ static void printProjectStatusReporting()`
- `+ static void TestingUserMenu()`

**Methods (Private Static):**

- `- static int readIntInRange(String prompt, int min, int max)`
- `- static int readPositiveInt(String prompt)`
- `- static double readNonNegativeDouble(String prompt)`
- `- static String readNonEmptyText(String prompt)`
- `- static String readExistingProjectId(String prompt)`
- `- static String readValidTaskStatus(String prompt)`
- `- static String readExistingUserId(String prompt)`
- `- static boolean isAdmin()`
- `- static boolean isLoggedIn()`
- `- static void checkAdminPermission(String action)`
- `- static void checkLoggedInPermission(String action)`

**Relationships:**

- Uses all Service classes
- Uses `Project`, `Task`, `User` models
- Uses `ValidationUtils`

---

### 14. ValidationUtils (Utility Class)

**Package:** `utils`  
**Type:** Final Utility Class

**Attributes:**

- `private static final Set<String> VALID_TASK_STATUSES`
- `private static final Set<String> VALID_PROJECT_TYPES`
- `private static final Set<String> VALID_USER_ROLES`
- `private static final Pattern EMAIL_PATTERN`

**Methods (Public Static):**

- `+ static boolean validateTaskStatus(String status)`
- `+ static boolean validateProjectType(String type)`
- `+ static boolean validateEmail(String email)`
- `+ static boolean validateTeamSize(int teamSize)`
- `+ static boolean validateBudget(double budget)`
- `+ static boolean validateBudgetRange(double minBudget, double maxBudget)`
- `+ static boolean validateUserRole(String role)`
- `+ static boolean hasText(String value)`
- `+ static boolean isValidPositiveNumber(String value)`
- `+ static boolean validateDateFormat(String dateString, String pattern)`
- `+ static boolean isValidText(String value)`

**Relationships:**

- Used by `ConsoleMenu`

---

## 🚀 Main Class

### 15. Main (Application Entry Point)

**Package:** (default/root)  
**Type:** Concrete Class

**Attributes:**

- None

**Methods:**

- `+ static void main(String[] args)`

**Relationships:**

- Creates and initializes all Service classes
- Sets up `ConsoleMenu` with services
- Starts application with `ConsoleMenu.initialLoginMenu()`

---

## 📊 UML Relationships Summary

### Inheritance Relationships:

1. `RegularUser` extends `User`
2. `AdminUser` extends `User`
3. `SoftwareProject` extends `Project`
4. `HardwareProject` extends `Project`

### Composition/Aggregation:

1. `UserService` contains `List<User>`
2. `ProjectService` contains `List<Project>` and `TaskService`
3. `TaskService` contains `List<Task>`
4. `ReportService` contains `ProjectService` and `TaskService`
5. `Project` contains `List<String>` (assignedUserIds)

### Association/Dependency:

1. `Task` → `Project` (via projectId)
2. `Task` → `User` (via assignedUserId)
3. `Project` → `User` (via assignedUserIds list)
4. `ConsoleMenu` → All Service classes
5. `ConsoleMenu` → All Model classes
6. `ConsoleMenu` → `ValidationUtils`
7. `ProjectService` → `ConsoleMenu` (for display)
8. `ProjectService` → `TaskService` (for reports)
9. `ReportService` → `ProjectService` and `TaskService`
10. `ReportService` → `ProjectStatusReportDto`

---

## 📐 UML Diagram Notation Guide

### Visibility Modifiers:

- `+` = Public
- `-` = Private
- `#` = Protected
- `~` = Package/Default

### Class Types:

- `<<abstract>>` = Abstract Class (User, Project)
- `<<interface>>` = Interface (if any)
- Regular class = Concrete Class

### Relationship Types:

- **Inheritance:** `--|>` (solid line with triangle)
- **Composition:** `--<>` (solid line with filled diamond)
- **Aggregation:** `--<>` (solid line with empty diamond)
- **Association:** `-->` (solid line with arrow)
- **Dependency:** `..>` (dashed line with arrow)

---

## 🎯 Key Design Patterns

1. **Inheritance:** User hierarchy and Project hierarchy
2. **Service Layer:** Separation of business logic (Services) from presentation (ConsoleMenu)
3. **DTO Pattern:** ProjectStatusReportDto for data transfer
4. **Singleton-like Services:** Services manage their own repositories
5. **Factory Pattern:** UserService creates RegularUser/AdminUser instances

---

## 📝 Notes for UML Diagram Creation

1. **Package Structure:**

   - Group classes by package (models, services, utils)
   - Main class in root/default package

2. **Abstract Classes:**

   - Mark `User` and `Project` as abstract
   - Show abstract methods in italics

3. **Static Members:**

   - Mark static attributes/methods with underline or {static} notation

4. **Multiplicity:**

   - `UserService` → `User`: 1..\* (one to many)
   - `ProjectService` → `Project`: 1..\* (one to many)
   - `TaskService` → `Task`: 1..\* (one to many)
   - `Project` → `User`: \* (many via assignedUserIds)
   - `Task` → `User`: 0..1 (optional assignment)

5. **Cardinality:**
   - One UserService manages many Users
   - One ProjectService manages many Projects
   - One TaskService manages many Tasks
   - One Project can have many Tasks
   - One Task belongs to one Project
   - One Task can be assigned to one User (optional)
   - One Project can have many assigned Users

---

This documentation provides all the information needed to create comprehensive UML class diagrams, sequence diagrams, and component diagrams for the Task Management System.
