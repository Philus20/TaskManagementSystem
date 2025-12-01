
package models;

public class SoftwareProject extends Project {
    private String programmingLanguage;

    public SoftwareProject( String name, String description, String type, int teamSize, String programmingLanguage, double budject) {
        super( name, description, type, teamSize,budject);
        this.programmingLanguage = programmingLanguage;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    @Override
    public void displayAttributes() {
        // Optional: leave empty or call ConsoleMenu.displayProjects(this)
    }
}
