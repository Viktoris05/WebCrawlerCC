package at.aau.cc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class MarkdownStorageTest {

    @TempDir
    Path tempDir;

    Path testFilePath;
    MarkdownStorage testStorage;
    String testSingleLine;
    String[] testMultipleLines;

    void createTestData() {
        testFilePath = tempDir.resolve("test_report.md");
        testStorage = new MarkdownStorage(testFilePath.toString());

        testSingleLine = "--> broken link <a>www.404.com</a>";
        testMultipleLines = new String[]{"input: <a>www.test.com</a>", "<br>depth: 1", "# Heading 1"};
    }

    @Test
    void testInitClearsFile() throws IOException {
        // Making test file with garbage data
        testFilePath = tempDir.resolve("test_report.md");
        Files.writeString(testFilePath, "Old garbage data");

        // Calling createTestData() initializes testStorage, which has to clean old data
        createTestData();

        var resultSize = Files.size(testFilePath);
        var resultExists = Files.exists(testFilePath);

        Assertions.assertTrue(resultExists);
        Assertions.assertEquals(0, resultSize);
    }

    @Test
    void writeLine() throws IOException {
        createTestData();

        testStorage.writeLine(testSingleLine);

        var resultList = Files.readAllLines(testFilePath);
        var resultSize = resultList.size();
        var resultLine = resultList.get(0);

        Assertions.assertEquals(1, resultSize);
        Assertions.assertEquals(testSingleLine, resultLine);
    }

    @Test
    void writeLines() throws IOException {
        createTestData();

        testStorage.writeLines(testMultipleLines);

        var resultList = Files.readAllLines(testFilePath);
        var resultSize = resultList.size();

        Assertions.assertEquals(3, resultSize);
        Assertions.assertEquals(testMultipleLines[0], resultList.get(0));
        Assertions.assertEquals(testMultipleLines[1], resultList.get(1));
        Assertions.assertEquals(testMultipleLines[2], resultList.get(2));
    }

    @Test
    void appendBehavior() throws IOException {
        createTestData();

        testStorage.writeLine("First line");
        testStorage.writeLines(new String[]{"Second line", "Third line"});

        var resultList = Files.readAllLines(testFilePath);
        var resultSize = resultList.size();

        Assertions.assertEquals(3, resultSize);
        Assertions.assertEquals("First line", resultList.get(0));
        Assertions.assertEquals("Second line", resultList.get(1));
        Assertions.assertEquals("Third line", resultList.get(2));
    }
}