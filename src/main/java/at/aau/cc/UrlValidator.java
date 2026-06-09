package at.aau.cc;

import java.net.URI;

public class UrlValidator {
    private final URI[] domains;

    public UrlValidator(URI[] domains) {
        this.domains = domains;
    }

    public boolean isValid(URI url) {
        if (url == null || url.getScheme() == null) {
            return false;
        }

        if (url.getFragment() != null || url.getScheme().equalsIgnoreCase("mailto") || url.getScheme().equalsIgnoreCase("javascript")) {
            return false;
        }

        if (!url.getScheme().equalsIgnoreCase("https") && !url.getScheme().equalsIgnoreCase("http")) return false;

        return containedWithinDomains(url);
    }


    public boolean containedWithinDomains(URI url) {
        if (domains != null && domains.length > 0) {
            for (URI domain : domains) {
                if (url.getHost().equals(domain.getHost())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}