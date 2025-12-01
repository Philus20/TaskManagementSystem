# Task Management System - User Documentation

## Table of Contents
1. [Overview](#overview)
2. [Getting Started](#getting-started)
3. [User Roles and Permissions](#user-roles-and-permissions)
4. [Features Guide](#features-guide)
5. [Step-by-Step Usage](#step-by-step-usage)
6. [Menu Navigation](#menu-navigation)
7. [Examples](#examples)

---

## Overview

The Task Management System is a Java-based console application designed to help teams manage projects and tasks efficiently. The system supports:

- **Project Management**: Create and manage Software and Hardware projects
- **Task Management**: Create, assign, update, and track tasks
- **User Management**: Role-based access control with Admin and Regular User roles
- **Reporting**: View project status reports and completion rates
- **User Assignment**: Assign users to projects and tasks

---

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- A Java IDE (IntelliJ IDEA, Eclipse, etc.) or command line compiler

### Running the Application

1. **Compile the project**:
   ```bash
   javac -d out src/**/*.java
   ```

2. **Run the application**:
   ```bash
   java -cp out Main
   ```

3. **Initial Screen**: When the app starts, you'll see the welcome menu with three options:
   - Login
   - Sign Up
   - Continue Without Account (Limited Access)

---

## User Roles and Permissions

### Admin User
**Permissions:**
- ✅ View all projects and tasks
- ✅ Create projects
- ✅ Add tasks
- ✅ **Update tasks** (Admin only)
- ✅ **Delete tasks** (Admin only)
- ✅ Assign users to projects and tasks
- ✅ View status reports

### Regular User
**Permissions:**
- ✅ View all projects and tasks
- ✅ Create projects
- ✅ Add tasks
- ❌ Update tasks (Access Denied)
- ❌ Delete tasks (Access Denied)
- ✅ Assign users to projects and tasks
- ✅ View status reports

### Guest (No Account)
**Permissions:**
- ✅ View projects and tasks (read-only)
- ❌ Create projects (must login)
- ❌ Add tasks (must login)
- ❌ Update/Delete tasks (must login)

---

## Features Guide

### 1. User Management

#### Sign Up
1. Select **"2. Sign Up"** from the initial menu
2. Enter your name
3. Enter a valid email address
4. Choose your role: **Regular** or **Admin**
5. You'll be automatically logged in after signup
6. Your User ID will be displayed (format: U0001, U0002, etc.)

#### Login
1. Select **"1. Login"** from the initial menu
2. View the list of available users (if any)
3. Enter your User ID
4. You'll be logged in and redirected to the main menu

#### Switch User
1. From the main menu, select **"4. Switch User"**
2. Options available:
   - Create New User
   - Login with Existing User
   - View All Users
   - Display Current User
   - Logout
   - Back to Main Menu

---

### 2. Project Management

#### Create a Project
1. From the main menu, select **"1. Manage Projects"**
2. Select **"1. View All Projects"** (or use Testing Mode)
3. Or use **"5. Testing"** from main menu → **"1. Create New Project"**
4. Choose project type:
   - **Software**: Requires programming language
   - **Hardware**: Requires hardware type
5. Enter project details:
   - Project Name
   - Description
   - Team Size
   - Budget
6. Project ID is auto-generated (format: P0001, P0002, etc.)

**Note**: You must be logged in to create projects.

#### View Projects
1. From main menu → **"1. Manage Projects"**
2. Options:
   - **View All Projects**: See all projects
   - **Software Projects Only**: Filter by Software type
   - **Hardware Projects Only**: Filter by Hardware type
   - **Search by Budget Range**: Filter by budget range
   - **Assign User to Project**: Assign users to projects

#### View Project Details
1. From Project Catalog → Select any view option
2. After viewing projects, you'll be prompted to enter a project ID
3. Enter the project ID to see:
   - Project information
   - Assigned users
   - Associated tasks
   - Completion rate
   - Task management options

---

### 3. Task Management

#### Add a Task
1. From main menu → **"2. Manage Tasks"**
2. Select **"1. Add New Task"**
3. Enter task details:
   - Task Name
   - Project ID (must exist)
   - Task Status (Pending, In Progress, or Completed)
4. **Assign a User** (optional):
   - View available users
   - Enter User ID to assign
   - Or enter "0" to skip assignment
5. Task ID is auto-generated (format: T0001, T0002, etc.)
6. Assigned user information is displayed

**Note**: You must be logged in to add tasks.

#### Update Task Status (Admin Only)
1. From main menu → **"2. Manage Tasks"**
2. Select **"2. Update Task Status"**
3. Enter the Task ID
4. Enter new status: **Pending**, **In Progress**, or **Completed**
5. Task status is updated

**Note**: Only Admin users can update tasks. Regular users will see an "Access Denied" message.

#### Delete a Task (Admin Only)
1. From main menu → **"2. Manage Tasks"**
2. Select **"3. Remove Task"**
3. Enter the Task ID
4. Task is deleted

**Note**: Only Admin users can delete tasks. Regular users will see an "Access Denied" message.

#### View Tasks
- Tasks are displayed when viewing project details
- Task list shows:
  - Task ID
  - Task Name
  - Status
  - Assigned User Name
  - Assigned User Email

---

### 4. User Assignment

#### Assign User to Project
1. From main menu → **"1. Manage Projects"**
2. Select **"5. Assign User to Project"**
3. Enter Project ID
4. View available users
5. Enter User ID to assign
6. Confirmation message is displayed

#### Assign User to Task
- When creating a task, you'll be prompted to assign a user
- View the list of available users
- Enter User ID or "0" to skip

---

### 5. Status Reports

#### View Project Status Report
1. From main menu → **"3. View Status Reports"**
2. View comprehensive report showing:
   - Project ID
   - Project Name
   - Total Tasks
   - Completed Tasks
   - Average Completion Percentage

---

## Step-by-Step Usage

### First Time Setup

1. **Start the application**
   ```
   java -cp out Main
   ```

2. **Create an Admin account**
   - Select "2. Sign Up"
   - Enter name: "John Admin"
   - Enter email: "admin@example.com"
   - Enter role: "Admin"
   - Note your User ID (e.g., U0001)

3. **Create a project**
   - You're automatically logged in after signup
   - Select "1. Manage Projects" → "1. View All Projects"
   - Or use "5. Testing" → "1. Create New Project"
   - Create a Software project with details

4. **Add a task**
   - Select "2. Manage Tasks" → "1. Add New Task"
   - Enter task details
   - Assign yourself or another user

### Daily Workflow

#### For Regular Users:
1. **Login** with your User ID
2. **View projects** to see current work
3. **Add tasks** as needed
4. **View status reports** to track progress

#### For Admin Users:
1. **Login** with your User ID
2. **Create/Manage projects**
3. **Add tasks** and assign users
4. **Update task statuses** as work progresses
5. **Delete tasks** when no longer needed
6. **View reports** for project overview

---

## Menu Navigation

### Main Menu
```
JAVA PROJECT MANAGEMENT SYSTEM
1. Manage Projects
2. Manage Tasks
3. View Status Reports
4. Switch User
5. Testing
6. Exit
```

### Project Catalog Menu
```
PROJECT CATALOG
1. View All Projects
2. Software Projects Only
3. Hardware Projects Only
4. Search by Budget Range
5. Assign User to Project
```

### Task Management Menu
```
TASK MANAGEMENT
1. Add New Task
2. Update Task Status (Admin only)
3. Remove Task (Admin only)
4. View Project Status Report
5. Back to Main Menu
```

### Switch User Menu
```
SWITCH USER
1. Create New User
2. Login with Existing User
3. View All Users
4. Display Current User
5. Logout
6. Back to Main Menu
```

---

## Examples

### Example 1: Creating a Software Project

```
1. Login or Sign Up
2. Main Menu → "1. Manage Projects"
3. Testing Mode → "1. Create New Project"
4. Select "1. Software"
5. Enter:
   - Name: "E-commerce Platform"
   - Description: "Build online shopping system"
   - Team Size: 5
   - Budget: 50000
   - Language: "Java"
6. Project created with ID: P0001
```

### Example 2: Adding a Task with User Assignment

```
1. Main Menu → "2. Manage Tasks" → "1. Add New Task"
2. Enter:
   - Task Name: "Design Database Schema"
   - Project ID: P0001
   - Status: "In Progress"
3. View available users:
   - U0001: John Admin (admin@example.com) - Admin User
   - U0002: Jane User (jane@example.com) - Regular User
4. Enter User ID: U0002
5. Task created with ID: T0001
   Assigned User: Jane User (jane@example.com) - Regular User
```

### Example 3: Regular User Trying to Update Task

```
1. Login as Regular User (U0002)
2. Main Menu → "2. Manage Tasks" → "2. Update Task Status"
3. System Response:
   "Access Denied: Only Admin users can update tasks.
    Your current role: Regular User"
```

### Example 4: Viewing Project Details

```
1. Main Menu → "1. Manage Projects" → "1. View All Projects"
2. View project list
3. Enter project ID: P0001
4. See:
   - Project Name: E-commerce Platform
   - Project Type: Software
   - Team Size: 5
   - Budget: 50000.0
   - Assigned Users: (list if any)
   - Associated Tasks:
     * T0001: Design Database Schema - In Progress
       Assigned: Jane User (jane@example.com)
   - Completion Rate: 0.0%
```

---

## Tips and Best Practices

1. **Always note your User ID** after signup - you'll need it to login
2. **Admin users** should be created first for full system access
3. **Assign users to tasks** when creating them for better tracking
4. **Use project details view** to see all related information at once
5. **Regular users** can view and add, but need Admin for updates/deletes
6. **Continue without account** option is useful for quick viewing only

---

## Troubleshooting

### "Access Denied" Messages
- **Update/Delete operations**: Only Admin users can perform these. Login as Admin or contact an Admin user.
- **Add operations**: You must be logged in. Use "Switch User" → "Login with Existing User" or "Sign Up".

### "User not found"
- Check that you entered the correct User ID
- Use "Switch User" → "View All Users" to see available User IDs

### "Project not found"
- Verify the Project ID is correct
- Use "View All Projects" to see available Project IDs

### "No users available"
- Create a user first using "Sign Up" or "Switch User" → "Create New User"

---

## System Information

- **Project ID Format**: P0001, P0002, P0003...
- **Task ID Format**: T0001, T0002, T0003...
- **User ID Format**: U0001, U0002, U0003...
- **Valid Task Statuses**: Pending, In Progress, Completed
- **Project Types**: Software, Hardware
- **User Roles**: Regular User, Admin User

---

## Support

For issues or questions:
1. Check this documentation
2. Review the error messages (they provide helpful guidance)
3. Ensure you're using the correct User ID and Project/Task IDs
4. Verify your user role has the required permissions

---

**Version**: 1.0  
**Last Updated**: 2024

