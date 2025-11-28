package models;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String role;

    private static int idCounter = 1;

    public User(String name, String email, String role) {
        this.id = "U" + String.format("%04d", idCounter++);
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public abstract void displayRole();

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Email: %s, Role: %s", id, name, email, role);
    }
}
