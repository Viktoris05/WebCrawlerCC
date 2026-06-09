package at.aau.cc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

class HeaderExtractorTest {

    @Test
    void extractHeaders() {
        WebPage document = null;
        String[][] expectedOutput = new String[][]{{"Hello, I'm Benji.", "1"}, {"Links","2"}, {"Projects","2"}, {"Code", "2"}};

        var result = HeaderExtractor.extractHeaders(document);

        Assertions.assertEquals(Arrays.deepToString(expectedOutput), Arrays.deepToString(result));
    }
}