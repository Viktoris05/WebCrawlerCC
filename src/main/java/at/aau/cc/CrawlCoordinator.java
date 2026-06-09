package at.aau.cc;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlCoordinator {
    public WebCrawler webCrawler;
    private final UrlValidator validator;
    private final Phaser phaser = new Phaser(1); //1 for the main Thread
    private final ExecutorService executor;
    private final int maxDepth;
    private final Set<URI> visitedUrls = ConcurrentHashMap.newKeySet();
    private final static Logger logger = Logger.getLogger(CrawlCoordinator.class.getName());

    public CrawlCoordinator(int maxDepth, WebCrawler crawler, UrlValidator validator, ExecutorService executor) {
        this.maxDepth = maxDepth;
        this.webCrawler = crawler;
        this.validator = validator;
        this.executor = executor;
    }

    public void start(URI startUrl) {
        var startNode = UrlNode.createRootNode(startUrl);
        submitIfValid(startNode);

        phaser.arriveAndAwaitAdvance();

        webCrawler.writeReport(startNode);
    }

    private void submitIfValid(UrlNode node) {

        if (node.depth() > maxDepth) return;

        if (!validator.isValid(node.url())) return;

        //.add is atomic
        if (!visitedUrls.add(node.url())) return;

        submitNode(node);
    }

    private void submitNode(UrlNode node) {

        phaser.register();

        executor.submit(() -> {
            try {
                processNode(node);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                phaser.arriveAndDeregister();
            }
        });
    }

    private void processNode(UrlNode current) {
        printProgress(current);

        List<URI> links = webCrawler.process(current);

        for (URI link : links) {
            var childNode = current.createChildNode(link);
            submitIfValid(childNode);
        }

    }

    private void printProgress(UrlNode current) {
        logger.log(Level.INFO, "Thread: {0} | Reading URL: {1} | Reading depth: {2}", new Object[]{Thread.currentThread().getName(), current.url(), current.depth()});
    }

}
