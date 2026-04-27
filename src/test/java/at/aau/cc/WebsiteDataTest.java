package at.aau.cc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.lang.Integer.parseInt;


class WebsiteDataTest {

    String testLink;
    int testDepth;
    String testHeader;
    int testHeaderTag;
    String[][] testHeaders;

    WebsiteData testWebsiteData;
    void createTestData() {
        testLink = "testlink.com";
        testDepth = 2;
        testHeader = "Hello there!";
        testHeaderTag = 1;
        testHeaders = new String[][]{{testHeader, "1"}, {"General Kenobi!", "2"}, {"You are a bold one.", "3"}};
        testWebsiteData = new WebsiteData(testLink,testDepth, testHeaders);
    }


    @Test
    void getLink() {
        createTestData();

        var result = testWebsiteData.link();

        Assertions.assertEquals(testLink, result);
    }

    @Test
    void getCurrentDepth() {
        createTestData();

        var result = testWebsiteData.currentDepth();

        Assertions.assertEquals(testDepth, result);
    }

    @Test
    void getHeaders() {
        createTestData();

        var result = testWebsiteData.headers();

        Assertions.assertArrayEquals(testHeaders, result);
    }

    @Test
    void getHeader() {
        createTestData();

        var result = testWebsiteData.getHeader(0);

        Assertions.assertEquals(testHeader, result);
    }

    @Test
    void getHeaderTag() {
        createTestData();
        int inputHeader = parseInt(testHeaders[0][1]);

        var result = testWebsiteData.getHeaderTag(0);

        Assertions.assertEquals(inputHeader, result);
    }
}