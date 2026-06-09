package at.aau.cc;

// Base exception class for all crawling-related errors (SRP: specialized domain exception)
public class CrawlException extends RuntimeException {
    public CrawlException(String message, Throwable cause) {
        super(message, cause);
    }
}