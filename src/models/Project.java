
package models;

import java.util.List;

public abstract class Project {
    public String id;
    public String name;
    public String description;
    public String type;
    public int teamSize;
    public double budget;



    public Project(String id, String name, String description, String type, int teamSize,double budget) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.teamSize = teamSize;
        this.budget = budget;
    }
    public String getId (){

        return id;
    }

    public String getType (){

        return type;
    }

    public double getBudget (){

        return budget;
    }

    public abstract void displayAttributes();


}
