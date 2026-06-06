package at.aau.cc;

// Specific exception thrown when the application detects it is offline or lacks network access
public class OfflineException extends CrawlException {
    public OfflineException(String message, Throwable cause) {
        super(message, cause);
    }
}