package at.aau.cc;

// Specific exception thrown when an HTTP error status code (e.g., 404, 500) is returned
public class PageHttpException extends CrawlException {
    private final int statusCode;

    public PageHttpException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}