package models;

public class RegularUser extends User {

    public RegularUser(String name, String email) {
        super(name, email, "Regular User");
    }

    @Override
    public void displayRole() {
        System.out.println("Logged in as: Regular User");
        System.out.println("User: " + name + " (" + email + ")");
    }
}
