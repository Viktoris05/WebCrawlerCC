package at.aau.cc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;

class WebCrawlerTest {
    WebCrawler webCrawler;
    private Storage storage;
    private PageFetcher fetcher;
    private UrlNode urlNode;

    @BeforeEach
    void setUp() {
        urlNode = UrlNode.createRootNode(URI.create("https://benji.link/"));
        storage = Mockito.mock(Storage.class);
        fetcher = Mockito.mock(PageFetcher.class);
        webCrawler = new WebCrawler(storage, fetcher);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(storage, fetcher);

    }

    @Test
    void processShouldNotThrowException() {
        WebPage webPage = Mockito.mock(WebPage.class);
        Mockito.when(fetcher.fetch(any())).thenReturn(webPage);


        Assertions.assertDoesNotThrow(() -> webCrawler.process(urlNode));
    }

    @Test
    void writeReport() {
    }
}