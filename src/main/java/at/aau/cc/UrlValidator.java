package at.aau.cc;

public class UrlValidator {
    private final String[] domains;

    public UrlValidator(String[] domains) {
        this.domains = domains;
    }

    public boolean isValid(String url) {
        // Exclude empty strings, anchors (with #), email links, and JavaScript actions
        if (url == null || url.isEmpty() || url.contains("#") || url.startsWith("mailto:") || url.startsWith("javascript:")) {
            return false;
        }

        // If domain filters are provided, the URL must contain at least one of the specified domains
        if (domains != null && domains.length > 0) {
            for (String domain : domains) {
                if (url.contains(domain)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}