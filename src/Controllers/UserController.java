package Controllers;

import interfaces.IUserService;
import models.User;
import utils.Printer;
import utils.ValidationUtils;

/**
 * UserController following Single Responsibility Principle (SRP)
 * - Only responsible for user-related user interactions
 * - Delegates business logic to services
 */
public class UserController {
    private final IUserService userService;
    private final ValidationUtils in;
    private final Printer out;

    public UserController(IUserService userService, ValidationUtils in, Printer out) {
        this.userService = userService;
        this.in = in;
        this.out = out;
    }

    /**
     * Sign up a new user
     */
    public User signUp() {
        out.printTitle("SIGN UP");
        String name = in.readNonEmptyText("Enter User Name: ");

        String email;
        while (true) {
            email = in.readNonEmptyText("Enter User Email: ");
            if (!ValidationUtils.validateEmail(email)) {
                out.printMessage("Invalid email format. Please enter a valid email address.");
                continue;
            }
            break;
        }

        String role;
        while (true) {
            role = in.readNonEmptyText("Enter User Role (Regular/Admin): ");
            if (!ValidationUtils.validateUserRole(role)) {
                out.printMessage("Invalid role. Supported roles: Regular, Admin. Try again.");
                continue;
            }
            break;
        }

        User user;
        if (role.equalsIgnoreCase("Admin")) {
            user = userService.createAdminUser(name, email);
        } else {
            user = userService.createRegularUser(name, email);
        }

        out.printMessage("User created successfully!");
        out.printMessage("User Details: " + user.toString());
        out.printMessage("");

        // Auto-login after signup
        userService.login(user.getId());
        out.printMessage("You have been automatically logged in.");
        out.printMessage("");

        return user;
    }

    /**
     * Login user
     */
    public boolean login() {
        out.printTitle("LOGIN");

        // Show available users if any exist
        User[] users = userService.getAllUsers();
        if (users != null && users.length > 0) {
            out.printUsersTable(users);
        }

        String userId = in.readNonEmptyText("Enter User ID: ");
        User user = userService.login(userId);

        if (user != null) {
            out.printMessage("Login successful!");
            user.displayRole();
            out.printMessage("");
            return true;
        } else {
            out.printMessage("User not found with ID: " + userId);
            out.printMessage("");
            return false;
        }
    }

    /**
     * Create a new user (without auto-login)
     */
    public void createUser() {
        out.printTitle("CREATE USER PROFILE");
        String name = in.readNonEmptyText("Enter User Name: ");

        String email;
        while (true) {
            email = in.readNonEmptyText("Enter User Email: ");
            if (!ValidationUtils.validateEmail(email)) {
                out.printMessage("Invalid email format. Please enter a valid email address.");
                continue;
            }
            break;
        }

        String role;
        while (true) {
            role = in.readNonEmptyText("Enter User Role (Regular/Admin): ");
            if (!ValidationUtils.validateUserRole(role)) {
                out.printMessage("Invalid role. Supported roles: Regular, Admin. Try again.");
                continue;
            }
            break;
        }

        User user;
        if (role.equalsIgnoreCase("Admin")) {
            user = userService.createAdminUser(name, email);
        } else {
            user = userService.createRegularUser(name, email);
        }

        out.printMessage("User created successfully!");
        out.printMessage("User Details: " + user.toString());
        out.printMessage("");
    }

    /**
     * Display all users
     */
    public void displayAllUsers() {
        out.printTitle("ALL USERS");
        User[] users = userService.getAllUsers();

        if (users == null || users.length == 0) {
            out.printMessage("No users found.");
        } else {
            out.printUsersTable(users);
        }
        out.printMessage("");
    }

    /**
     * Display current user
     */
    public void displayCurrentUser() {
        out.printTitle("CURRENT USER");
        userService.displayCurrentUser();
        out.printMessage("");
    }

    /**
     * Logout current user
     */
    public void logout() {
        userService.logout();
        out.printMessage("Logged out successfully.");
        out.printMessage("");
    }

    /**
     * Switch user menu
     */
    public void switchUserMenu() {
        out.printTitle("SWITCH USER");
        out.printMessage("1. Create New User");
        out.printMessage("2. Login with Existing User");
        out.printMessage("3. View All Users");
        out.printMessage("4. Display Current User");
        out.printMessage("5. Logout");
        out.printMessage("6. Back to Main Menu");

        int choice = in.readIntInRange("Enter your choice __", 1, 6);

        switch (choice) {
            case 1:
                createUser();
                switchUserMenu();
                break;
            case 2:
                login();
                break;
            case 3:
                displayAllUsers();
                break;
            case 4:
                displayCurrentUser();
                break;
            case 5:
                logout();
                break;
            case 6:
                // Return to main menu - handled by MenuRouter
                break;
        }
    }

    /**
     * Initial login menu
     */
    public int initialLoginMenu() {
        out.printTitle("WELCOME TO PROJECT MANAGEMENT SYSTEM");
        out.printMessage("");
        out.printMessage("1. Login");
        out.printMessage("2. Sign Up");
        out.printMessage("3. Continue Without Account (Limited Access)");
        out.printMessage("");

        return in.readIntInRange("Enter your choice: ", 1, 3);
    }
}
