# UML Quick Reference - Classes, Attributes & Methods

## 📋 Models Package

| Class | Type | Key Attributes | Key Methods | Extends |
|-------|------|---------------|-------------|---------|
| **User** | Abstract | id, name, email, role, idCounter | getId(), getName(), getEmail(), getRole(), displayRole()* | - |
| **RegularUser** | Concrete | (inherited) | RegularUser(), displayRole() | User |
| **AdminUser** | Concrete | (inherited) | AdminUser(), displayRole() | User |
| **Project** | Abstract | id, name, description, type, teamSize, budget, assignedUserIds, idCounter | getId(), getType(), getBudget(), displayAttributes()*, assignUser(), removeUser() | - |
| **SoftwareProject** | Concrete | programmingLanguage | SoftwareProject(), getProgrammingLanguage(), displayAttributes() | Project |
| **HardwareProject** | Concrete | hardwareType | HardwareProject(), getHardwareType(), displayAttributes() | Project |
| **Task** | Concrete | taskName, taskId, taskStatus, projectId, assignedUserId, idCounter | Task() [2 constructors], setTaskStatus(), getTaskName(), getTaskId(), getTaskStatus(), getProjectId(), getAssignedUserId() | - |
| **ProjectStatusReportDto** | DTO | projectId, projectName, Tasks, Completed | ProjectStatusReportDto() | - |

*Abstract method

---

## 🔧 Services Package

| Class | Type | Key Attributes | Key Methods | Manages |
|-------|------|---------------|-------------|---------|
| **UserService** | Concrete | users (List<User>), currentUser | createRegularUser(), createAdminUser(), login(), logout(), getCurrentUser(), getAllUsers(), getUserById() | User objects |
| **ProjectService** | Concrete | projectsRepository (List<Project>), taskService | addProject(), getAllProjects(), filterProjectByType(), getProjectById(), assignUserToProject(), removeUserFromProject() | Project objects |
| **TaskService** | Concrete | tasks (List<Task>) | addTask(), getAllTasks(), getTaskById(), updateTaskStatus(), deleteTask(), getTasksByProjectId(), calculateCompletionRate() | Task objects |
| **ReportService** | Concrete | projectService, taskService | getProjectStatusReport(), calculateAverageProjectStatusReport() | Reports |

---

## 🎨 Utils Package

| Class | Type | Key Attributes | Key Methods | Purpose |
|-------|------|---------------|-------------|---------|
| **ConsoleMenu** | Concrete | projectService, taskService, reportService, userService, scanner (all static) | mainMenu(), initialLoginMenu(), signUpMenu(), loginUserMenu(), projectCatalog(), taskMainMenu(), addingNewTaskMenu(), updatingTask(), removingTask() | UI/User Interface |
| **ValidationUtils** | Utility | VALID_TASK_STATUSES, VALID_PROJECT_TYPES, VALID_USER_ROLES, EMAIL_PATTERN (all static final) | validateTaskStatus(), validateEmail(), validateTeamSize(), validateBudget(), validateUserRole(), hasText() | Input Validation |

---

## 🚀 Main Class

| Class | Type | Attributes | Methods | Purpose |
|-------|------|------------|---------|---------|
| **Main** | Entry Point | None | main(String[] args) | Application entry point |

---

## 🔗 Key Relationships

### Inheritance Hierarchy:
```
User (abstract)
├── RegularUser
└── AdminUser

Project (abstract)
├── SoftwareProject
└── HardwareProject
```

### Composition/Aggregation:
- `UserService` → `List<User>` (1 to many)
- `ProjectService` → `List<Project>` (1 to many)
- `TaskService` → `List<Task>` (1 to many)
- `ReportService` → `ProjectService` + `TaskService` (composition)

### Associations:
- `Task` → `Project` (via projectId) - Many to One
- `Task` → `User` (via assignedUserId) - Many to One (optional)
- `Project` → `User` (via assignedUserIds) - Many to Many
- `ConsoleMenu` → All Services (dependency)
- `ProjectService` → `TaskService` (uses for reports)

---

## 📊 Method Count Summary

| Class | Public Methods | Private Methods | Total Methods |
|-------|---------------|-----------------|---------------|
| User | 6 | 0 | 6 |
| RegularUser | 2 | 0 | 2 |
| AdminUser | 2 | 0 | 2 |
| Project | 7 | 0 | 7 |
| SoftwareProject | 2 | 0 | 2 |
| HardwareProject | 2 | 0 | 2 |
| Task | 8 | 0 | 8 |
| ProjectStatusReportDto | 1 | 0 | 1 |
| UserService | 8 | 0 | 8 |
| ProjectService | 10 | 0 | 10 |
| TaskService | 7 | 0 | 7 |
| ReportService | 2 | 0 | 2 |
| ConsoleMenu | ~25 | ~10 | ~35 |
| ValidationUtils | 10 | 0 | 10 |
| Main | 1 | 0 | 1 |

---

## 🎯 Visibility Legend

- `+` = Public
- `-` = Private  
- `#` = Protected
- `~` = Package/Default
- `{static}` = Static member
- `{abstract}` = Abstract method

---

## 📝 Notes

1. **ID Generation**: All IDs are auto-generated using static counters:
   - User IDs: U0001, U0002...
   - Project IDs: P0001, P0002...
   - Task IDs: T0001, T0002...

2. **User Assignment**: 
   - Tasks can have one assigned user (optional)
   - Projects can have multiple assigned users

3. **Role-Based Access**:
   - Admin: Full access (CRUD)
   - Regular User: View and Add only
   - Guest: View only

4. **Service Layer Pattern**: Services manage their own repositories and business logic

5. **DTO Pattern**: ProjectStatusReportDto used for data transfer between services

