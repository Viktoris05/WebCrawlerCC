package at.aau.cc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MarkdownStorage implements Storage {
    private final String filePath;

    public MarkdownStorage(String filePath) {
        this.filePath = filePath;
        // Before writing, the file gets cleaned, or create a new file
        try {
            new FileWriter(filePath, false).close();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing file: " + filePath + ": " + e.getMessage());
        }
    }

    public void writeLines(String[] lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing lines to file: " + e.getMessage());
        }
    }

    public void writeLine(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error writing line to file: " + e.getMessage());
        }
    }
}
