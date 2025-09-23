package edu.ccrm.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A generic service for file I/O operations using NIO.2 and Streams.
 * 
 * @param <T> The type of data to be handled.
 */
public class FileService<T> {

    private static final String DATA_DIRECTORY = "data";
    private final Path filePath;

    public FileService(String fileName) {
        Path dataDir = Paths.get(DATA_DIRECTORY);
        // Create the directory if it doesn't exist
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                System.err.println("Could not create data directory: " + e.getMessage());
            }
        }
        this.filePath = dataDir.resolve(fileName);
    }

    public List<T> readData(Function<String, T> lineMapper) throws IOException {
        if (!Files.exists(filePath)) {
            return List.of(); // Return empty list if file doesn't exist
        }
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines.map(lineMapper).collect(Collectors.toList());
        }
    }

    public void writeData(List<String> lines) throws IOException {
        Files.write(filePath, lines);
    }
}