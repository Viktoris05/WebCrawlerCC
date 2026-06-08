package at.aau.cc;

import javax.xml.validation.Validator;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

public class CrawlCoordinator{
    public WebCrawler webCrawler;
    private UrlValidator validator;
    private final Phaser phaser = new Phaser(1); //1 for the main Thread
    private final ExecutorService executor;
    private int maxDepth;
    private final Set<URI> visitedUrls = ConcurrentHashMap.newKeySet();

    public CrawlCoordinator(int maxDepth, WebCrawler crawler, UrlValidator validator, ExecutorService executor) {
        this.maxDepth = maxDepth;
        this.webCrawler = crawler;
        this.validator = validator;
        this.executor = executor;
    }

    public void start(URI startUrl){
        submitIfValid(new UrlNode(startUrl, 1));

        phaser.arriveAndAwaitAdvance();
    }

    private void submitIfValid(UrlNode node) {

        if(node.depth() > maxDepth) return;

        if(!validator.isValid(node.url())) return;

        //.add is atomic
        if(!visitedUrls.add(node.url())) return;

        submitNode(node);
    }

    private void submitNode(UrlNode node) {

        phaser.register();

        executor.submit(() -> {
            try {
                processNode(node);
            }catch(Exception e) {
                throw new RuntimeException(e);
            }finally {
                phaser.arriveAndDeregister();
            }
        });
    }

    private void processNode(UrlNode current){
        System.out.println("Processing URL: " + current.url());

        printProgress(current);
        try {
            List<URI> links = webCrawler.process(current);
        } catch (OfflineException e) {
            // If offline, inform the user, log it to the report, and terminate crawling to prevent useless retries
            handleOfflineError(current, e);
            System.err.println("\n[CRITICAL] Crawling aborted completely: The system is offline. Check your internet connection.");
        } catch (PageHttpException e) {
            handleHttpError(current, e);
        } catch (CrawlTimeoutException e) {
            handleTimeoutError(current, e);
        } catch (CrawlException e) {
            handleGenericCrawlError(current, e);
        }
    }


    private void handleOfflineError(UrlNode current, OfflineException e) {
        String errMsg = OutputFormat.formatOfflineError(current.url(), current.depth());
        //storage.writeLine(errMsg);
        System.out.println("\t".repeat(Math.max(0, current.depth() - 1)) + errMsg);
    }

    private void handleHttpError(UrlNode current, PageHttpException e) {
        String errMsg = OutputFormat.formatHttpError(current.url(), current.depth(), e.getStatusCode());
        //storage.writeLine(errMsg);
        System.out.println("\t".repeat(Math.max(0, current.depth() - 1)) + errMsg);
    }

    private void handleTimeoutError(UrlNode current, CrawlTimeoutException e) {
        String errMsg = OutputFormat.formatTimeoutError(current.url(), current.depth());
        //storage.writeLine(errMsg);
        System.out.println("\t".repeat(Math.max(0, current.depth() - 1)) + errMsg);
    }

    private void handleGenericCrawlError(UrlNode current, CrawlException e) {
        String errMsg = OutputFormat.formatBrokenLink(current.url(), current.depth());
        //storage.writeLine(errMsg + " (Reason: " + e.getMessage() + ")");
        System.out.println("\t".repeat(Math.max(0, current.depth() - 1)) + errMsg);
    }

    private void printProgress(UrlNode current) {
        System.out.println("\t".repeat(current.depth() - 1) + "Reading URL: " + current.url() + " | Depth: " + current.depth());
    }

}
