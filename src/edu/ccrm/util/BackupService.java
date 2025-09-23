package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class BackupService {

    private final Path dataDirectory = Paths.get("data");
    private final Path backupsDirectory = Paths.get("backups");

    public void backupData() throws IOException {
        // 1. Ensure the main backups directory exists
        if (Files.notExists(backupsDirectory)) {
            Files.createDirectories(backupsDirectory);
        }

        // 2. Create a timestamped folder for this specific backup
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path backupPath = backupsDirectory.resolve(timestamp);
        Files.createDirectories(backupPath);

        // 3. Check if the data directory exists before trying to copy
        if (Files.notExists(dataDirectory) || !Files.isDirectory(dataDirectory)) {
            System.out.println("Data directory not found. Nothing to back up.");
            return;
        }

        // 4. Copy files from 'data' to the new timestamped backup folder
        try (Stream<Path> files = Files.list(dataDirectory)) {
            files.forEach(sourceFile -> {
                try {
                    Path destinationFile = backupPath.resolve(sourceFile.getFileName());
                    Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // Use a runtime exception to handle errors within the lambda
                    throw new RuntimeException("Could not copy file: " + sourceFile, e);
                }
            });
        }

        System.out.println("Backup created successfully at: " + backupPath);

        // 5. Calculate and print the size of the new backup directory
        long size = calculateDirectorySize(backupPath);
        System.out.println("Total backup size: " + (size / 1024) + " KB");
    }

    /**
     * Recursively calculates the total size of a directory.
     * 
     * @param path The path of the directory.
     * @return The total size in bytes.
     * @throws IOException If an I/O error occurs.
     */
    private long calculateDirectorySize(Path path) throws IOException {
        long size = 0;
        try (Stream<Path> walk = Files.walk(path)) {
            size = walk
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            System.err.println("Failed to get size of " + p + ": " + e.getMessage());
                            return 0L;
                        }
                    })
                    .sum();
        }
        return size;
    }
}