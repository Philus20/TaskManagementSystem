package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for ValidationService
 * Testing regex validation for Week 3 requirements
 */
@DisplayName("ValidationService Tests")
class ValidationServiceTest {

    @Nested
    @DisplayName("Task ID Validation")
    class TaskIdValidation {
        
        @Test
        @DisplayName("Valid task IDs should pass validation")
        void testValidTaskIds() {
            assertTrue(ValidationService.isValidTaskId("T001"));
            assertTrue(ValidationService.isValidTaskId("T123"));
            assertTrue(ValidationService.isValidTaskId("T999"));
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"T1", "T12", "T1234", "t001", "T001A", "TX01", "", "null"})
        @DisplayName("Invalid task IDs should fail validation")
        void testInvalidTaskIds(String taskId) {
            if ("null".equals(taskId)) {
                assertFalse(ValidationService.isValidTaskId(null));
            } else {
                assertFalse(ValidationService.isValidTaskId(taskId));
            }
        }
    }
    
    @Nested
    @DisplayName("Project ID Validation")
    class ProjectIdValidation {
        
        @Test
        @DisplayName("Valid project IDs should pass validation")
        void testValidProjectIds() {
            assertTrue(ValidationService.isValidProjectId("P001"));
            assertTrue(ValidationService.isValidProjectId("P123"));
            assertTrue(ValidationService.isValidProjectId("P999"));
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"P1", "P12", "P1234", "p001", "P001A", "PX01", "", "null"})
        @DisplayName("Invalid project IDs should fail validation")
        void testInvalidProjectIds(String projectId) {
            if ("null".equals(projectId)) {
                assertFalse(ValidationService.isValidProjectId(null));
            } else {
                assertFalse(ValidationService.isValidProjectId(projectId));
            }
        }
    }
    
    @Nested
    @DisplayName("User ID Validation")
    class UserIdValidation {
        
        @Test
        @DisplayName("Valid user IDs should pass validation")
        void testValidUserIds() {
            assertTrue(ValidationService.isValidUserId("U001"));
            assertTrue(ValidationService.isValidUserId("U123"));
            assertTrue(ValidationService.isValidUserId("U999"));
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"U1", "U12", "U1234", "u001", "U001A", "UX01", "", "null"})
        @DisplayName("Invalid user IDs should fail validation")
        void testInvalidUserIds(String userId) {
            if ("null".equals(userId)) {
                assertFalse(ValidationService.isValidUserId(null));
            } else {
                assertFalse(ValidationService.isValidUserId(userId));
            }
        }
    }
    
    @Nested
    @DisplayName("Email Validation")
    class EmailValidation {
        
        @Test
        @DisplayName("Valid emails should pass validation")
        void testValidEmails() {
            assertTrue(ValidationService.isValidEmail("user@example.com"));
            assertTrue(ValidationService.isValidEmail("test.email@domain.co.uk"));
            assertTrue(ValidationService.isValidEmail("user+tag@example.org"));
            assertTrue(ValidationService.isValidEmail("user123@test-domain.com"));
        }
        
        @ParameterizedTest
        @ValueSource(strings = {
            "invalid-email", 
            "@domain.com", 
            "user@", 
            "user@.com", 
            "user@domain", 
            "user space@domain.com",
            "",
            "null"
        })
        @DisplayName("Invalid emails should fail validation")
        void testInvalidEmails(String email) {
            if ("null".equals(email)) {
                assertFalse(ValidationService.isValidEmail(null));
            } else {
                assertFalse(ValidationService.isValidEmail(email));
            }
        }
    }
    
    @Nested
    @DisplayName("Task Status Validation")
    class TaskStatusValidation {
        
        @Test
        @DisplayName("Valid task statuses should pass validation")
        void testValidTaskStatuses() {
            assertTrue(ValidationService.isValidTaskStatus("Pending"));
            assertTrue(ValidationService.isValidTaskStatus("In Progress"));
            assertTrue(ValidationService.isValidTaskStatus("Completed"));
            assertTrue(ValidationService.isValidTaskStatus("pending"));
            assertTrue(ValidationService.isValidTaskStatus("completed"));
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"Invalid", "In-Progress", "Done", "Cancelled", "", "null"})
        @DisplayName("Invalid task statuses should fail validation")
        void testInvalidTaskStatuses(String status) {
            if ("null".equals(status)) {
                assertFalse(ValidationService.isValidTaskStatus(null));
            } else {
                assertFalse(ValidationService.isValidTaskStatus(status));
            }
        }
    }
    
    @Nested
    @DisplayName("Project Type Validation")
    class ProjectTypeValidation {
        
        @Test
        @DisplayName("Valid project types should pass validation")
        void testValidProjectTypes() {
            assertTrue(ValidationService.isValidProjectType("Software"));
            assertTrue(ValidationService.isValidProjectType("Hardware"));
            assertTrue(ValidationService.isValidProjectType("software"));
            assertTrue(ValidationService.isValidProjectType("HARDWARE"));
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"Network", "Database", "Web", "Mobile", "", "null"})
        @DisplayName("Invalid project types should fail validation")
        void testInvalidProjectTypes(String type) {
            if ("null".equals(type)) {
                assertFalse(ValidationService.isValidProjectType(null));
            } else {
                assertFalse(ValidationService.isValidProjectType(type));
            }
        }
    }
    
    @Nested
    @DisplayName("ID Number Extraction")
    class IdNumberExtraction {
        
        @Test
        @DisplayName("Should extract correct numbers from valid IDs")
        void testExtractIdNumber() {
            assertEquals(1, ValidationService.extractIdNumber("T001"));
            assertEquals(123, ValidationService.extractIdNumber("P123"));
            assertEquals(999, ValidationService.extractIdNumber("U999"));
        }
        
        @Test
        @DisplayName("Should throw exception for invalid IDs")
        void testExtractIdNumberInvalid() {
            assertThrows(IllegalArgumentException.class, () -> ValidationService.extractIdNumber(""));
            assertThrows(IllegalArgumentException.class, () -> ValidationService.extractIdNumber("T"));
            assertThrows(IllegalArgumentException.class, () -> ValidationService.extractIdNumber("TX01"));
        }
    }
    
    @Nested
    @DisplayName("Error Message Generation")
    class ErrorMessageGeneration {
        
        @Test
        @DisplayName("Should generate comprehensive error messages")
        void testGetValidationErrorMessage() {
            String errors = ValidationService.getValidationErrorMessage("T1", "P12", "U1234", "invalid-email");
            
            assertTrue(errors.contains("Invalid Task ID format"));
            assertTrue(errors.contains("Invalid Project ID format"));
            assertTrue(errors.contains("Invalid User ID format"));
            assertTrue(errors.contains("Invalid email format"));
        }
        
        @Test
        @DisplayName("Should return success message for valid inputs")
        void testGetValidationErrorMessageValid() {
            String message = ValidationService.getValidationErrorMessage("T001", "P123", "U456", "user@example.com");
            assertEquals("All validations passed.", message);
        }
    }
}
