package at.aau.cc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutputFormatTest {

    WebsiteData websiteData;
    String[][] headers;
    String link;
    int depth;

    void createTestData(int depth){
        headers = new String[][]{{"Test", "2"}, {"Test2", "1"}};
        websiteData = new WebsiteData("test.com",depth, headers);
    }

    void createTestLink(int testDepth){
        link = "https://google.com";
        depth = testDepth;
    }

    @Test
    void formatFirstLink(){
        createTestData(1);
        String[] expected = new String[]{"input: <a>test.com</a>", "<br>depth: 1", "## Test", "# Test2"};


        var result = OutputFormat.formatLink(websiteData, true);

        Assertions.assertEquals(expected[0], result[0]);
        Assertions.assertEquals(expected[1], result[1]);
        Assertions.assertEquals(expected[2], result[2]);
        Assertions.assertEquals(expected[3], result[3]);
    }

    @Test
    void formatLinkDepth2() {
        createTestData(2);
        String[] expected = new String[]{"<br>--> link to <a>test.com</a>", "<br>depth: 2", "## --> Test", "# --> Test2"};


        var result = OutputFormat.formatLink(websiteData, false);

        Assertions.assertEquals(expected[0], result[0]);
        Assertions.assertEquals(expected[1], result[1]);
        Assertions.assertEquals(expected[2], result[2]);
        Assertions.assertEquals(expected[3], result[3]);
    }

    @Test
    void formatLinkDepth4() {
        createTestData(4);
        String[] expected = new String[]{"<br>------> link to <a>test.com</a>", "<br>depth: 4", "## ------> Test", "# ------> Test2"};


        var result = OutputFormat.formatLink(websiteData, false);

        Assertions.assertEquals(expected[0], result[0]);
        Assertions.assertEquals(expected[1], result[1]);
        Assertions.assertEquals(expected[2], result[2]);
        Assertions.assertEquals(expected[3], result[3]);
    }

    @Test
    void formatLinkDepth0() {
        createTestData(0);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> OutputFormat.formatLink(websiteData, false));

        Assertions.assertEquals("Depth must be greater than zero", exception.getMessage());

    }

    @Test
    void formatBrokenLink() {
        createTestLink(2);
        String expectedLink = "<br>--> broken link to <a>" + link + "</a>";

        var result = OutputFormat.formatBrokenLink(link, depth);

        Assertions.assertEquals(expectedLink, result);
    }

    @Test
    void formatRecurringLink() {
        createTestLink(2);
        String expectedLink = "<br>--> <a>" + link + "</a>, already visited";

        var result = OutputFormat.formatRecurringLink(link, depth);

        Assertions.assertEquals(expectedLink, result);
    }

    @Test
    void formatLinkOnly() {
        createTestLink(2);
        String expectedLink = "<br>--> link to <a>" + link + "</a>";

        var result = OutputFormat.formatLinkOnly(link, depth);

        Assertions.assertEquals(expectedLink, result);
    }

    @Test
    void formatLinkOnlyWrongDepth() {
        createTestLink(1);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> OutputFormat.formatLinkOnly(link, depth));

        Assertions.assertEquals("Depth must be greater than zero", exception.getMessage());
    }
}