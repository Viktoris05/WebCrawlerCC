package at.aau.cc;

// Specific exception thrown when a socket timeout occurs during fetching
public class CrawlTimeoutException extends CrawlException {
    public CrawlTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}