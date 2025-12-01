# Quick Start Guide - Task Management System

## 🚀 Getting Started in 5 Minutes

### Step 1: Run the Application
```bash
java -cp out Main
```

### Step 2: Create Your Account
1. Select **"2. Sign Up"**
2. Enter your name and email
3. Choose role: **"Admin"** (for full access) or **"Regular"** (for view/add only)
4. **Note your User ID** (e.g., U0001) - you'll need it to login!

### Step 3: Create a Project
1. From main menu → **"5. Testing"** → **"1. Create New Project"**
2. Choose **Software** or **Hardware**
3. Fill in project details
4. Project created! (ID format: P0001, P0002...)

### Step 4: Add a Task
1. Main menu → **"2. Manage Tasks"** → **"1. Add New Task"**
2. Enter task name and project ID
3. Choose status: **Pending**, **In Progress**, or **Completed**
4. Assign a user (optional)
5. Task created! (ID format: T0001, T0002...)

---

## 📋 Common Tasks

### Login
- Main menu → **"4. Switch User"** → **"2. Login with Existing User"**
- Enter your User ID

### View Projects
- Main menu → **"1. Manage Projects"** → **"1. View All Projects"**

### View Tasks
- View a project → See all associated tasks with assigned users

### Update Task (Admin Only)
- Main menu → **"2. Manage Tasks"** → **"2. Update Task Status"**
- Enter Task ID and new status

### Delete Task (Admin Only)
- Main menu → **"2. Manage Tasks"** → **"3. Remove Task"**
- Enter Task ID

---

## 🔐 Permissions Quick Reference

| Action | Guest | Regular User | Admin |
|--------|-------|--------------|-------|
| View | ✅ | ✅ | ✅ |
| Add | ❌ | ✅ | ✅ |
| Update | ❌ | ❌ | ✅ |
| Delete | ❌ | ❌ | ✅ |

---

## 💡 Pro Tips

1. **Always login first** - Most features require authentication
2. **Create Admin account first** - For full system control
3. **Note your IDs** - User ID, Project ID, Task ID are needed for operations
4. **Assign users to tasks** - Better tracking and organization
5. **Use Testing Mode** - Quick access to create projects

---

## 🆘 Quick Troubleshooting

**"Access Denied"** → You need Admin role or must be logged in  
**"User not found"** → Check User ID in "Switch User" → "View All Users"  
**"Project not found"** → View projects list to get correct Project ID  

---

For detailed documentation, see [README.md](README.md)

