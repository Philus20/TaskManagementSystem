package models;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String role;

    /**
     * Constructor - ID should be set by service layer using IdGenerator
     * Following Dependency Inversion Principle
     */
    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
        // ID will be set by service layer
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
