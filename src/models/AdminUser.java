package models;

public class AdminUser extends User {

    public AdminUser(String name, String email) {
        super(name, email, "Admin User");
    }

    @Override
    public void displayRole() {
        System.out.println("Logged in as: Admin User");
        System.out.println("User: " + name + " (" + email + ")");
    }
}
