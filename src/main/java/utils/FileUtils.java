package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Utility class for file operations using NIO
 * Provides common file I/O operations with error handling
 */
public class FileUtils {
    
    /**
     * Create directory if it doesn't exist
     */
    public static boolean createDirectoryIfNotExists(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Write string content to file
     */
    public static boolean writeToFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            Files.writeString(path, content);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Read string content from file
     */
    public static String readFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return null;
            }
            return Files.readString(path);
        } catch (IOException e) {
            System.err.println("Failed to read from file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Check if file is empty
     */
    public static boolean isFileEmpty(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return true;
            }
            return Files.size(path) == 0;
        } catch (IOException e) {
            System.err.println("Failed to check file size: " + e.getMessage());
            return true;
        }
    }
    
    /**
     * Get file size in bytes
     */
    public static long getFileSize(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return -1;
            }
            return Files.size(path);
        } catch (IOException e) {
            System.err.println("Failed to get file size: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Delete file if it exists
     */
    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Read all lines from file
     */
    public static List<String> readAllLines(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return List.of();
            }
            return Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println("Failed to read lines from file: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Write lines to file
     */
    public static boolean writeLines(String filePath, List<String> lines) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, lines);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to write lines to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Async file write operation
     */
    public static CompletableFuture<Boolean> writeToFileAsync(String filePath, String content) {
        return CompletableFuture.supplyAsync(() -> writeToFile(filePath, content));
    }
    
    /**
     * Async file read operation
     */
    public static CompletableFuture<String> readFromFileAsync(String filePath) {
        return CompletableFuture.supplyAsync(() -> readFromFile(filePath));
    }
    
    /**
     * Stream lines from file
     */
    public static Stream<String> streamLines(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return Stream.empty();
            }
            return Files.lines(path);
        } catch (IOException e) {
            System.err.println("Failed to stream lines from file: " + e.getMessage());
            return Stream.empty();
        }
    }
    
    /**
     * Get file extension
     */
    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) {
            return "";
        }
        return filePath.substring(lastDotIndex + 1);
    }
    
    /**
     * Get file name without extension
     */
    public static String getFileNameWithoutExtension(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }
    
    /**
     * Backup file by copying with timestamp suffix
     */
    public static boolean backupFile(String filePath) {
        try {
            Path originalPath = Paths.get(filePath);
            if (!Files.exists(originalPath)) {
                return false;
            }
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            String backupPath = filePath + ".backup_" + timestamp;
            Path backupFilePath = Paths.get(backupPath);
            
            Files.copy(originalPath, backupFilePath);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to backup file: " + e.getMessage());
            return false;
        }
    }
}
