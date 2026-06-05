package at.aau.cc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Disabled //IntegrationTest
class WebCrawlerTest {
    static String link;
    static int maxDepth = 2;
    static String[] domains = new String[]{"https://benji.link"};
    static String fileName = "TestOutput.md";


    @BeforeAll
    static void start() {
        WebCrawler webCrawler = new WebCrawler(maxDepth, domains, fileName);
        link = "https://benji.link";

        webCrawler.start(link);
    }

    static boolean isInFile(String input){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null){
                if (line.contains(input)){
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Test
    void checkForLinks(){
        Assertions.assertTrue(isInFile("https://benji.link"));
        Assertions.assertTrue(isInFile("https://benji.link/links/"));

        Assertions.assertFalse(isInFile("https://github.com"));
    }

    @Test
    void checkForDepth(){
        Assertions.assertFalse(isInFile("<br>depth: 3"));

        Assertions.assertTrue(isInFile("<br>depth: 1"));
    }

    @Test
    void checkForHeaders(){
        Assertions.assertTrue(isInFile("# Hello, I'm Benji."));
        Assertions.assertTrue(isInFile("### --> Services"));
    }
}