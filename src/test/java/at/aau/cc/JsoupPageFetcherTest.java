package at.aau.cc;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsoupPageFetcherTest {

    @Test
    public void testFetch_WhenHttpError404_ShouldThrowPageHttpException() {
        // Arrange
        JsoupPageFetcher fetcher = new JsoupPageFetcher();
        // httpbin.org/status/404 is a reliable mock service that always returns a 404 HTTP status
        try {
            URI urlReturning404 = new URI("https://httpbin.org/status/404");


            // Act & Assert
            PageHttpException exception = assertThrows(PageHttpException.class, () -> fetcher.fetch(urlReturning404), "Fetching a 404 URL should throw PageHttpException");

            assertEquals(404, exception.getStatusCode(), "The status code should be 404");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFetch_WhenUnknownHost_ShouldThrowOfflineException() {
        // Arrange
        JsoupPageFetcher fetcher = new JsoupPageFetcher();
        // A domain that is guaranteed not to exist will trigger an UnknownHostException
        final URI nonExistentUrl;
        try {
            nonExistentUrl = new URI("http://this-domain-surely-does-not-exist-99999.com");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Act & Assert
        assertThrows(OfflineException.class, () -> fetcher.fetch(nonExistentUrl), "Fetching an unknown host should be mapped to an OfflineException");
    }
}