package at.aau.cc;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            checkArgsLength(args);

            int depthLimit = parseDepth(args[1]);
            checkDepthLimit(depthLimit);
            String[] domains = getDomains(args);

            UrlValidator validator = new UrlValidator(domains);

            String startUrl = formatStartLink(args[0]);
            if (!validator.isValid(startUrl)) {
                throw new IllegalArgumentException("Invalid initial link: " + startUrl);
            }

            if(!validator.isValidDomains()){
                throw new IllegalArgumentException("Invalid domains: " + Arrays.toString(domains));
            }

            if(!validator.containedWithinDomains(startUrl)){
                throw new IllegalArgumentException("Initial link (" + startUrl + ") not within Domains: " + Arrays.toString(domains));
            }

            System.out.println("START SCANNING");

            WebCrawler crawler = new WebCrawler(depthLimit, domains, "Output.md");
            crawler.start(startUrl);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Arguments: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }
    }


    private static void checkArgsLength(String[] args){
        if(args.length<3){
            throw new IllegalArgumentException("Wrong number of Arguments. Usage: java Main <URL> <Depth> <Domain1> <Domain2> ... <DomainN>");
        }
    }

    private static int parseDepth(String depthLimit){
        try {
            return Integer.parseInt(depthLimit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Depth limit must be an integer");
        }
    }

    private static void checkDepthLimit(int depthLimit){
        if(depthLimit<1 || depthLimit>10) {
            throw new IllegalArgumentException("Depth limit must be between 1 and 10");
        }
    }

    private static String normalizeProtocol(String input) {
        if (input == null) return "";
        String cleaned = input.trim();

        if (!cleaned.startsWith("http://") && !cleaned.startsWith("https://")) {
            cleaned = "https://" + cleaned;
        }
        return cleaned;
    }

    private static String formatStartLink(String link) {
        String normalized = normalizeProtocol(link);
        return normalized.endsWith("/") ? normalized : normalized + "/";
    }

    private static String[] getDomains(String[] args) {
        String[] domains = new String[args.length - 2];
        for (int i = 0; i < domains.length; i++) {
            domains[i] = normalizeProtocol(args[i + 2]);
        }
        return domains;
    }
}
