package at.aau.cc;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkExtractorTest {

    URI link;
    WebPage document;
    List<URI> expectedOutput = new ArrayList<>();

    void setUp() throws IOException {
        link = URI.create("https://benji.link/links");
        document = Mockito.mock(WebPage.class);


        expectedOutput.add(URI.create("https://benji.link/"));
        expectedOutput.add(URI.create("https://benji.link/"));
        expectedOutput.add(URI.create("https://github.com/hibenji"));
        expectedOutput.add(URI.create("https://up.benji.link"));
        expectedOutput.add(URI.create("https://short.benji.link"));
        expectedOutput.add(URI.create("https://paste.benji.link"));
        expectedOutput.add(URI.create("https://suchnode.net"));
        expectedOutput.add(URI.create("https://benji.link/kreuzel/linopt.html"));


    }

    @Test
    void extractAbsHref() throws IOException {
        setUp();
        Mockito.when(document.select("a[href]")).thenReturn(expectedOutput.stream().map(uri ->
                (WebElement) new MockWebElement("test", "a", uri.toString(), null)
        ).toList());

        var result = LinkExtractor.extract(document, link);

        assertEquals(expectedOutput, result);

    }

    @Test
    void extractOnClick() throws IOException {
        setUp();
        Mockito.when(document.select("button[onclick]")).thenReturn(expectedOutput.stream().map(uri ->
                (WebElement) new MockWebElement("test", "button", null, uri.toString())
        ).toList());

        var result = LinkExtractor.extract(document, link);

        assertEquals(expectedOutput, result);

    }
}