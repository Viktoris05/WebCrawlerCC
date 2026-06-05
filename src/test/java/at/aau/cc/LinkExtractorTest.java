package at.aau.cc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkExtractorTest {

    String link;
    Document document;
    List<String> expectedOutput = new ArrayList<>();

    void setUp() throws IOException {
        link = "https://benji.link/links";
        document = Jsoup.connect(link).get();

        expectedOutput.add("https://benji.link/");
        expectedOutput.add("https://benji.link/");
        expectedOutput.add("https://github.com/hibenji");
        expectedOutput.add("https://up.benji.link");
        expectedOutput.add("https://short.benji.link");
        expectedOutput.add("https://paste.benji.link");
        expectedOutput.add("https://suchnode.net");
        expectedOutput.add("https://benji.link/kreuzel/linopt.html");
    }

    @Test
    void extract() throws IOException {
        setUp();

        var result = LinkExtractor.extract(document, link);

        assertEquals(expectedOutput, result);

    }
}