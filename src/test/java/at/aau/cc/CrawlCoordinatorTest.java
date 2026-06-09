package at.aau.cc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

class CrawlCoordinatorTest {
    CrawlCoordinator coordinator;
    private WebCrawler crawler;
    private UrlValidator validator;
    private ExecutorService executor;


    @BeforeEach
    void setUp() {
        crawler = Mockito.mock(WebCrawler.class);
        validator = Mockito.mock(UrlValidator.class);
        executor = Executors.newFixedThreadPool(3);
        coordinator = new CrawlCoordinator(2, crawler, validator, executor);

        Mockito.when(validator.isValid(any())).thenReturn(true);
        Mockito.when(crawler.process(any())).thenReturn(List.of());
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(crawler, validator);
        executor.shutdown();
    }

    @Test
    void start() {
        URI startUrl = URI.create("http://localhost");

        coordinator.start(startUrl);


        Mockito.verify(crawler).writeReport(argThat(argument -> argument.url().equals(startUrl)));
    }
}