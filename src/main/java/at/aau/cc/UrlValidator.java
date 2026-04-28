package at.aau.cc;

public class UrlValidator {
    private final String[] domains;

    public UrlValidator(String[] domains) {
        this.domains = domains;
    }

    public boolean isValid(String url) {
        if (url == null || url.isEmpty() || url.contains("#") || url.startsWith("mailto:") || url.startsWith("javascript:")) {
            return false;
        }

        if (url.contains(" ") || !url.contains(".")) return false;
        if (!url.startsWith("http://") && !url.startsWith("https://")) return false;

        return containedWithinDomains(url);
    }

    public boolean isValidDomains(){
        for(String domain : domains){
            if(!isValid(domain)) return false;
        }
        return true;
    }

    public boolean containedWithinDomains(String url) {
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