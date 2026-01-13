package services;

import interfaces.IdGenerator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GenerateProjectId implements IdGenerator {
    private static int counter = 1;
    private static final Pattern PROJECT_ID_PATTERN = Pattern.compile("P\\d{3}");

    @Override
    public String generate() {
        return "P" + String.format("%03d", counter++);
    }
    
    /**
     * Get current counter value without incrementing
     */
    public static int getCurrentCounter() {
        return counter;
    }
    
    /**
     * Set counter to a specific value (useful when loading existing projects)
     */
    public static void setCounter(int value) {
        counter = Math.max(1, value); // Ensure counter is at least 1
    }
    
    /**
     * Update counter based on existing project IDs
     */
    public static void updateCounterFromExistingIds(List<String> existingIds) {
        int maxId = 0;
        for (String id : existingIds) {
            if (id != null && id.matches("P\\d{3}")) {
                int idNum = Integer.parseInt(id.substring(1));
                maxId = Math.max(maxId, idNum);
            }
        }
        counter = Math.max(counter, maxId + 1);
    }
    
    @Override
    public int elementIndex(String id) {
        if (!isValidProjectId(id)) {
            throw new IllegalArgumentException("Invalid project ID format: " + id + ". Expected format: P001");
        }
        return Integer.parseInt(id.substring(1)) - 1; // Convert P001 -> 0, P002 -> 1, etc.
    }

    public static boolean isValidProjectId(String id) {
        if (id == null) return false;
        Matcher matcher = PROJECT_ID_PATTERN.matcher(id);
        return matcher.matches();
    }
}
