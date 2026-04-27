package at.aau.cc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebsiteDataTest {

    @BeforeAll
    static void setUp(){
        String[][] headersOne = {{"Hello there!", "h1"}, {"General Kenobi!", "h2"}, {"You are a bold one.", "h3"}};
        WebsiteData websiteOne = new WebsiteData("",1,headersOne);

        String[][] headersTwo = {{"I don't like sand.", "h1"}, {"It's rough,", "h2"}, {"coarse", "h2"}, {"and it gets everywhere", "h2"}};
        WebsiteData websiteTwo = new WebsiteData("",2,headersTwo);

        String[][] headersThree = {{"My Powers have doubled since the last time we met, Count!", "h1"}, {"Good, twice the pride, double the fall.", "h5"}};
        WebsiteData websiteThree = new WebsiteData("",3,headersThree);
    }

    @Test
    void getLink() {

    }

    @Test
    void setLink() {
    }

    @Test
    void getCurrentDepth() {
    }

    @Test
    void setCurrentDepth() {
    }

    @Test
    void getHeaders() {
    }

    @Test
    void getHeader() {
    }

    @Test
    void setHeaders() {
    }
}