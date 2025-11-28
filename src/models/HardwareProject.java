
package models;

public class HardwareProject extends Project {
    private String hardwareType;

    public HardwareProject( String name, String description, String type, int teamSize, String hardwareType,double budject) {
        super( name, description, type, teamSize,budject);
        this.hardwareType = hardwareType;
    }

    public String getHardwareType() {
        return hardwareType;
    }

    @Override
    public void displayAttributes() {
        // Optional: leave empty or call ConsoleMenu.displayProjects(this)
    }
}
