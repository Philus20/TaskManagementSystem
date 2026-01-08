package models;

public abstract class Project {
    private  String id;
    private final String name;
    private final String description;
    private final String type;
    private final int teamSize;
    private final double budget;

    public Project(String id, String name, String description, String type, int teamSize, double budget) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.teamSize = teamSize;
        this.budget = budget;
    }

//This are my setters
    public void setId(String id) {  this.id = id; }


    //These are my Gettere methods
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public int getTeamSize() { return teamSize; }
    public double getBudget() { return budget; }

    public abstract void displayProjects();


}
