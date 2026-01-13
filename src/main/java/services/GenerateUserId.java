package services;

import interfaces.IdGenerator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GenerateUserId  implements IdGenerator{

    private static int counter = 1;
    private static final Pattern USER_ID_PATTERN = Pattern.compile("U\\d{3}");

    @Override
    public String generate() {
        return "U" + String.format("%03d", counter++);
    }
    
    /**
     * Get current counter value without incrementing
     */
    public static int getCurrentCounter() {
        return counter;
    }
    
    /**
     * Set counter to a specific value (useful when loading existing users)
     */
    public static void setCounter(int value) {
        counter = Math.max(1, value); // Ensure counter is at least 1
    }
    
    /**
     * Update counter based on existing user IDs
     */
    public static void updateCounterFromExistingIds(List<String> existingIds) {
        int maxId = 0;
        for (String id : existingIds) {
            if (id != null && id.matches("U\\d{3}")) {
                int idNum = Integer.parseInt(id.substring(1));
                maxId = Math.max(maxId, idNum);
            }
        }
        counter = Math.max(counter, maxId + 1);
    }


    @Override
    public int elementIndex(String id) {
        if (!isValidUserId(id)) {
            throw new IllegalArgumentException("Invalid user ID format: " + id + ". Expected format: U001");
        }
        return Integer.parseInt(id.substring(1)) - 1; // Convert U001 -> 0, U002 -> 1, etc.
    }
    
    public static boolean isValidUserId(String id) {
        if (id == null) return false;
        Matcher matcher = USER_ID_PATTERN.matcher(id);
        return matcher.matches();
    }
}
