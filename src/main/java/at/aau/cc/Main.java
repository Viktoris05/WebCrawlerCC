package at.aau.cc;

import java.net.URI;

public class Main {
    private static final int MIN_CRAWL_DEPTH = 1;
    private static final int MAX_CRAWL_DEPTH = 10;
    private static final int MIN_ARGS_LENGTH = 3;

    public static void main(String[] args) {
        try {
            checkArgsLength(args);

            int depthLimit = parseDepth(args[1]);
            checkDepthLimit(depthLimit);
            String[] domains = getDomains(args);


            //String normalizedUrl = normalizeURL(args[0]);
            //String startUrl = appendTrailingSlash(args[0]);

            //String inputLink = args[0];
            URI startUrl = new URI("https://" + args[0].trim());
            //startUrl = normalizeURL(startUrl);



            System.out.println("START SCANNING");

            UrlValidator validator = new UrlValidator(domains);
            Storage storage = new MarkdownStorage("Output.md");
            PageFetcher fetcher = new JsoupPageFetcher();

            WebCrawler crawler = new WebCrawler(depthLimit, validator, storage, fetcher);
            crawler.start(startUrl);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Arguments: " + e.getMessage());
        } catch (Exception f) {
            System.err.println("Unexpected error occurred: " + f.getMessage());
        }
    }


    private static void checkArgsLength(String[] args){
        if(args.length < MIN_ARGS_LENGTH){
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
        if(depthLimit < MIN_CRAWL_DEPTH || depthLimit > MAX_CRAWL_DEPTH) {
            throw new IllegalArgumentException("Depth limit must be between " + MIN_CRAWL_DEPTH + " and " + MAX_CRAWL_DEPTH);
        }
    }

//    private static String normalizeURL(String input) throws URISyntaxException {
//        if (input == null) return null;
//        if(input.getProtocol() == null) {
//            try {
//                input = new URI("https://" + input).toURL();
//            } catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return input;
//    }

//    private static String appendTrailingSlash(String link) {
//        if (link == null || link.isEmpty()) return link;
//        return link.endsWith("/") ? link : link + "/";
//    }

    private static String[] getDomains(String[] args) {
        String[] domains = new String[args.length - 2];
        for (int i = 0; i < domains.length; i++) {
            // For domains only normalize protocol
            //domains[i] = normalizeURL(args[i + 2]);
        }
        return domains;
    }
}
