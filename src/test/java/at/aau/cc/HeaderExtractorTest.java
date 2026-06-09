package at.aau.cc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class HeaderExtractorTest {
    WebPage document;

    @BeforeEach
    void setUp() {
        document = Mockito.mock(WebPage.class);

    }

    @AfterEach
    void tearDown() {
        Mockito.reset(document);
    }

    @Test
    void extractHeaders() {

        String[][] expectedOutput = new String[][]{{"Hello, I'm Benji.", "1"}, {"Links", "2"}, {"Projects", "2"}, {"Code", "2"}};
        when(document.select(anyString())).thenReturn(Arrays.stream(expectedOutput).map(o -> (WebElement) new MockWebElement(o[0], "h" + o[1], null, null)).toList());

        var result = HeaderExtractor.extractHeaders(document);

        Assertions.assertEquals(Arrays.deepToString(expectedOutput), Arrays.deepToString(result));
    }
}