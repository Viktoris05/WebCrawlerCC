package at.aau.cc;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final int MIN_CRAWL_DEPTH = 1;
    private static final int MAX_CRAWL_DEPTH = 10;
    private static final int MIN_ARGS_LENGTH = 4;
    private static final int MIN_THREADS_AMOUNT = 1;
    private static final int MAX_THREADS_AMOUNT = 128;
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            checkArgsLength(args);

            URI startUrl = parseStartUrl(args[0]);
            int depthLimit = parseDepth(args[1]);
            int threadsAmount = parseThreadAmount(args[2]);

            checkDepthLimit(depthLimit);
            checkThreadAmountLimit(threadsAmount);

            URI[] domains = parseDomains(args);


            logger.info("START SCANNING");

            UrlValidator validator = new UrlValidator(domains);
            Storage storage = new MarkdownStorage("Output.md");
            PageFetcher fetcher = new JsoupPageFetcher();

            ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
            WebCrawler crawler = new WebCrawler(storage, fetcher);
            CrawlCoordinator coordinator = new CrawlCoordinator(depthLimit, crawler, validator, executor);

            coordinator.start(startUrl);

            executor.shutdown();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Arguments: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred: " + e.getMessage(), e);
        }
    }

    private static URI parseStartUrl(String startUrl) {
        String parsedUrl = startUrl.trim();

        if (!parsedUrl.startsWith("https://") && !parsedUrl.startsWith("http://")) {
            parsedUrl = "https://" + parsedUrl;
        }

        var uri = URI.create(parsedUrl);
        return uri.normalize();
    }


    private static void checkArgsLength(String[] args) {
        if (args.length < MIN_ARGS_LENGTH) {
            throw new IllegalArgumentException("Wrong number of Arguments. Usage: java Main <URL> <Depth> <Domain1> <Domain2> ... <DomainN>");
        }
    }

    private static int parseDepth(String depthLimit) {
        try {
            return Integer.parseInt(depthLimit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Depth limit must be an integer");
        }
    }

    private static int parseThreadAmount(String threadsAmount) {
        try {
            return Integer.parseInt(threadsAmount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Threads amount must be an integer");
        }
    }

    private static void checkDepthLimit(int depthLimit) {
        if (depthLimit < MIN_CRAWL_DEPTH || depthLimit > MAX_CRAWL_DEPTH) {
            throw new IllegalArgumentException("Depth limit must be between " + MIN_CRAWL_DEPTH + " and " + MAX_CRAWL_DEPTH);
        }
    }

    private static void checkThreadAmountLimit(int threadsAmount) {
        if (threadsAmount < MIN_THREADS_AMOUNT || threadsAmount > MAX_THREADS_AMOUNT) {
            throw new IllegalArgumentException("Thread Amount Limit must be between " + MIN_THREADS_AMOUNT + " and " + MAX_THREADS_AMOUNT);
        }
    }


    private static URI[] parseDomains(String[] args) {
        URI[] domains = new URI[args.length - 2];
        for (int i = 0; i < domains.length; i++) {
            // For domains only normalize protocol
            domains[i] = URI.create(args[i + 2]);
        }
        return domains;
    }
}
