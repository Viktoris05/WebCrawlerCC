package at.aau.cc;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;

class HeaderExtractorTest {

    @Test
    void extractHeaders() throws IOException {
        Document document = Jsoup.connect("https://benji.link/").get();
        String[][] expectedOutput = new String[][]{{"Hello, I'm Benji.", "1"}, {"Links","2"}, {"Projects","2"}, {"Code", "2"}};

        var result = HeaderExtractor.extractHeaders(document);

        Assertions.assertEquals(Arrays.deepToString(expectedOutput), Arrays.deepToString(result));
    }
}