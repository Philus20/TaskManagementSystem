package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import utils.ValidationUtils;

/**
 * Comprehensive JUnit 5 tests for ValidationUtils
 * Tests cover:
 * - Task status validation
 * - Project type validation
 * - Email validation
 * - Team size validation
 * - Budget validation
 * - Budget range validation
 * - User role validation
 * - Text validation (hasText)
 * - Date format validation
 */
class ValidationUtilsTest {

    /**
     * Test: validateTaskStatus - valid statuses
     */
    @Test
    void testValidateTaskStatus_ValidStatuses() {
        assertTrue(ValidationUtils.validateTaskStatus("Pending"),
                "'Pending' should be a valid task status");
        assertTrue(ValidationUtils.validateTaskStatus("In Progress"),
                "'In Progress' should be a valid task status");
        assertTrue(ValidationUtils.validateTaskStatus("Completed"),
                "'Completed' should be a valid task status");

        // Case insensitive
        assertTrue(ValidationUtils.validateTaskStatus("pending"),
                "'pending' (lowercase) should be valid");
        assertTrue(ValidationUtils.validateTaskStatus("IN PROGRESS"),
                "'IN PROGRESS' (uppercase) should be valid");
        assertTrue(ValidationUtils.validateTaskStatus("completed"),
                "'completed' (lowercase) should be valid");
    }

    /**
     * Test: validateTaskStatus - invalid statuses
     */
    @Test
    void testValidateTaskStatus_InvalidStatuses() {
        assertFalse(ValidationUtils.validateTaskStatus("Invalid"),
                "'Invalid' should not be a valid task status");
        assertFalse(ValidationUtils.validateTaskStatus("Done"),
                "'Done' should not be a valid task status");
        assertFalse(ValidationUtils.validateTaskStatus("Started"),
                "'Started' should not be a valid task status");
        assertFalse(ValidationUtils.validateTaskStatus(null),
                "null should not be a valid task status");
        assertFalse(ValidationUtils.validateTaskStatus(""),
                "Empty string should not be a valid task status");
        assertFalse(ValidationUtils.validateTaskStatus("   "),
                "Whitespace-only string should not be a valid task status");
    }

    /**
     * Test: validateProjectType - valid types
     */
    @Test
    void testValidateProjectType_ValidTypes() {
        assertTrue(ValidationUtils.validateProjectType("Software"),
                "'Software' should be a valid project type");
        assertTrue(ValidationUtils.validateProjectType("Hardware"),
                "'Hardware' should be a valid project type");

        // Case insensitive
        assertTrue(ValidationUtils.validateProjectType("software"),
                "'software' (lowercase) should be valid");
        assertTrue(ValidationUtils.validateProjectType("HARDWARE"),
                "'HARDWARE' (uppercase) should be valid");
        assertTrue(ValidationUtils.validateProjectType("SoFtWaRe"),
                "'SoFtWaRe' (mixed case) should be valid");
    }

    /**
     * Test: validateProjectType - invalid types
     */
    @Test
    void testValidateProjectType_InvalidTypes() {
        assertFalse(ValidationUtils.validateProjectType("Invalid"),
                "'Invalid' should not be a valid project type");
        assertFalse(ValidationUtils.validateProjectType("Web"),
                "'Web' should not be a valid project type");
        assertFalse(ValidationUtils.validateProjectType("Mobile"),
                "'Mobile' should not be a valid project type");
        assertFalse(ValidationUtils.validateProjectType(null),
                "null should not be a valid project type");
        assertFalse(ValidationUtils.validateProjectType(""),
                "Empty string should not be a valid project type");
    }

    /**
     * Test: validateEmail - valid emails
     */
    @Test
    void testValidateEmail_ValidEmails() {
        assertTrue(ValidationUtils.validateEmail("user@example.com"),
                "Valid email should pass validation");
        assertTrue(ValidationUtils.validateEmail("test.user@domain.co.uk"),
                "Email with subdomain should pass validation");
        assertTrue(ValidationUtils.validateEmail("user123@test-domain.com"),
                "Email with numbers and hyphens should pass validation");
        assertTrue(ValidationUtils.validateEmail("firstname.lastname@company.org"),
                "Email with dots should pass validation");
        assertTrue(ValidationUtils.validateEmail("user+tag@example.com"),
                "Email with plus sign should pass validation");
    }


    /**
     * Test: validateTeamSize - valid sizes
     */
    @Test
    void testValidateTeamSize_ValidSizes() {
        assertTrue(ValidationUtils.validateTeamSize(1),
                "Team size of 1 should be valid");
        assertTrue(ValidationUtils.validateTeamSize(10),
                "Team size of 10 should be valid");
        assertTrue(ValidationUtils.validateTeamSize(100),
                "Team size of 100 should be valid");
        assertTrue(ValidationUtils.validateTeamSize(50),
                "Team size of 50 should be valid");
    }

    /**
     * Test: validateTeamSize - invalid sizes
     */
    @Test
    void testValidateTeamSize_InvalidSizes() {
        assertFalse(ValidationUtils.validateTeamSize(0),
                "Team size of 0 should be invalid");
        assertFalse(ValidationUtils.validateTeamSize(-1),
                "Negative team size should be invalid");
        assertFalse(ValidationUtils.validateTeamSize(-100),
                "Large negative team size should be invalid");
    }

    /**
     * Test: validateBudget - valid budgets
     */
    @Test
    void testValidateBudget_ValidBudgets() {
        assertTrue(ValidationUtils.validateBudget(0.0),
                "Budget of 0.0 should be valid");
        assertTrue(ValidationUtils.validateBudget(100.0),
                "Budget of 100.0 should be valid");
        assertTrue(ValidationUtils.validateBudget(1000.50),
                "Budget with decimals should be valid");
        assertTrue(ValidationUtils.validateBudget(1000000.0),
                "Large budget should be valid");
    }

    /**
     * Test: validateBudget - invalid budgets
     */
    @Test
    void testValidateBudget_InvalidBudgets() {
        assertFalse(ValidationUtils.validateBudget(-1.0),
                "Negative budget should be invalid");
        assertFalse(ValidationUtils.validateBudget(-100.0),
                "Large negative budget should be invalid");
        assertFalse(ValidationUtils.validateBudget(-0.01),
                "Small negative budget should be invalid");
    }

    /**
     * Test: validateBudgetRange - valid ranges
     */
    @Test
    void testValidateBudgetRange_ValidRanges() {
        assertTrue(ValidationUtils.validateBudgetRange(0.0, 100.0),
                "Valid budget range should pass");
        assertTrue(ValidationUtils.validateBudgetRange(50.0, 50.0),
                "Equal min and max should be valid");
        assertTrue(ValidationUtils.validateBudgetRange(100.0, 200.0),
                "Normal range should be valid");
        assertTrue(ValidationUtils.validateBudgetRange(0.0, 0.0),
                "Zero range should be valid");
    }

    /**
     * Test: validateBudgetRange - invalid ranges
     */
    @Test
    void testValidateBudgetRange_InvalidRanges() {
        assertFalse(ValidationUtils.validateBudgetRange(-1.0, 100.0),
                "Negative min should be invalid");
        assertFalse(ValidationUtils.validateBudgetRange(100.0, -1.0),
                "Negative max should be invalid");
        assertFalse(ValidationUtils.validateBudgetRange(200.0, 100.0),
                "Min greater than max should be invalid");
        assertFalse(ValidationUtils.validateBudgetRange(-10.0, -5.0),
                "Both negative should be invalid");
    }

    /**
     * Test: validateUserRole - valid roles
     */
    @Test
    void testValidateUserRole_ValidRoles() {
        assertTrue(ValidationUtils.validateUserRole("Admin"),
                "'Admin' should be a valid user role");
        assertTrue(ValidationUtils.validateUserRole("Regular"),
                "'Regular' should be a valid user role");

        // Case insensitive
        assertTrue(ValidationUtils.validateUserRole("admin"),
                "'admin' (lowercase) should be valid");
        assertTrue(ValidationUtils.validateUserRole("REGULAR"),
                "'REGULAR' (uppercase) should be valid");
        assertTrue(ValidationUtils.validateUserRole("AdMiN"),
                "'AdMiN' (mixed case) should be valid");
    }

    /**
     * Test: validateUserRole - invalid roles
     */
    @Test
    void testValidateUserRole_InvalidRoles() {
        assertFalse(ValidationUtils.validateUserRole("Invalid"),
                "'Invalid' should not be a valid user role");
        assertFalse(ValidationUtils.validateUserRole("Guest"),
                "'Guest' should not be a valid user role");
        assertFalse(ValidationUtils.validateUserRole("Manager"),
                "'Manager' should not be a valid user role");
        assertFalse(ValidationUtils.validateUserRole(null),
                "null should not be a valid user role");
        assertFalse(ValidationUtils.validateUserRole(""),
                "Empty string should not be a valid user role");
    }

    /**
     * Test: hasText - valid text
     */
    @Test
    void testHasText_ValidText() {
        assertTrue(ValidationUtils.hasText("text"),
                "Non-empty string should have text");
        assertTrue(ValidationUtils.hasText("  text  "),
                "String with whitespace should have text after trim");
        assertTrue(ValidationUtils.hasText("123"),
                "Numeric string should have text");
        assertTrue(ValidationUtils.hasText("a"),
                "Single character should have text");
    }

    /**
     * Test: hasText - invalid text
     */
    @Test
    void testHasText_InvalidText() {
        assertFalse(ValidationUtils.hasText(null),
                "null should not have text");
        assertFalse(ValidationUtils.hasText(""),
                "Empty string should not have text");
        assertFalse(ValidationUtils.hasText("   "),
                "Whitespace-only string should not have text");
        assertFalse(ValidationUtils.hasText("\t\n"),
                "Tab and newline only should not have text");
    }

    /**
     * Test: validateDateFormat - valid dates
     */
    @Test
    void testValidateDateFormat_ValidDates() {
        assertTrue(ValidationUtils.validateDateFormat("2024-01-15", "yyyy-MM-dd"),
                "Valid date format should pass");
        assertTrue(ValidationUtils.validateDateFormat("15/01/2024", "dd/MM/yyyy"),
                "Valid date with different format should pass");
        assertTrue(ValidationUtils.validateDateFormat("2024-12-31", "yyyy-MM-dd"),
                "End of year date should pass");
        assertTrue(ValidationUtils.validateDateFormat("01/01/2024", "dd/MM/yyyy"),
                "Start of year date should pass");
    }



    /**
     * Test: validateTaskStatus - whitespace handling
     */
    @Test
    void testValidateTaskStatus_WhitespaceHandling() {
        assertTrue(ValidationUtils.validateTaskStatus("  Pending  "),
                "Task status with whitespace should be valid after trim");
        assertTrue(ValidationUtils.validateTaskStatus("  Completed  "),
                "Task status with whitespace should be valid after trim");
    }

    /**
     * Test: validateProjectType - whitespace handling
     */
    @Test
    void testValidateProjectType_WhitespaceHandling() {
        assertTrue(ValidationUtils.validateProjectType("  Software  "),
                "Project type with whitespace should be valid after trim");
        assertTrue(ValidationUtils.validateProjectType("  Hardware  "),
                "Project type with whitespace should be valid after trim");
    }



    /**
     * Test: validateBudgetRange - edge cases
     */
    @Test
    void testValidateBudgetRange_EdgeCases() {
        assertTrue(ValidationUtils.validateBudgetRange(0.0, 0.0),
                "Zero to zero range should be valid");
        assertTrue(ValidationUtils.validateBudgetRange(100.0, 100.0),
                "Same min and max should be valid");
        assertFalse(ValidationUtils.validateBudgetRange(100.0, 99.99),
                "Min slightly greater than max should be invalid");
    }

    /**
     * Test: validateUserRole - whitespace handling
     */
    @Test
    void testValidateUserRole_WhitespaceHandling() {
        assertTrue(ValidationUtils.validateUserRole("  Admin  "),
                "User role with whitespace should be valid after trim");
        assertTrue(ValidationUtils.validateUserRole("  Regular  "),
                "User role with whitespace should be valid after trim");
    }

    /**
     * Test: validateDateFormat - various formats
     */
    @Test
    void testValidateDateFormat_VariousFormats() {
        assertTrue(ValidationUtils.validateDateFormat("2024-01-15", "yyyy-MM-dd"),
                "ISO format should be valid");
        assertTrue(ValidationUtils.validateDateFormat("15/01/2024", "dd/MM/yyyy"),
                "European format should be valid");
        assertTrue(ValidationUtils.validateDateFormat("01/15/2024", "MM/dd/yyyy"),
                "US format should be valid");
        assertFalse(ValidationUtils.validateDateFormat("15-01-2024", "yyyy-MM-dd"),
                "Wrong format should fail");
    }
}
