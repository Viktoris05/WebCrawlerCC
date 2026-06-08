package at.aau.cc;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

public class WebCrawler {
    private final int maxDepth;
    private final UrlValidator validator;
    private final Set<URI> visitedUrls;
    //private final Stack<UrlNode> stack;
    private final Storage storage;
    private final PageFetcher fetcher;
    private final ExecutorService executor;
    private final Phaser phaser = new Phaser(1); //1 for the main Thread

    public WebCrawler(int maxDepth, UrlValidator validator, Storage storage, PageFetcher fetcher, ExecutorService executor) {
        this.maxDepth = maxDepth;
        this.validator = validator;
        this.storage = storage;
        this.fetcher = fetcher;
        this.visitedUrls = ConcurrentHashMap.newKeySet();
        //this.stack = new Stack<>();
        this.executor = executor;
    }

//    public void start(URI startUrl) {
//        pushIfValid(startUrl, 1);
//
//        while (!stack.isEmpty()) {
//            process(stack.pop());
//        }
//
//        //submitNode(new UrlNode(startUrl, 1));
//
//        phaser.arriveAndAwaitAdvance();
//
//    }



//    private void process(UrlNode current){
//        System.out.println("Processing URL: " + current.url);
//        //if (!isVisited(current)) {
//            //markVisited(current);
//
//            printProgress(current);
//            try {
//                WebPage doc = fetcher.fetch(current.url);
//                processAndSaveData(doc, current);
//                if (current.depth < maxDepth) {
//                    enqueueChildLinks(doc, current);
//                }
//            } catch (OfflineException e) {
//                // If offline, inform the user, log it to the report, and terminate crawling to prevent useless retries
//                handleOfflineError(current, e);
//                System.err.println("\n[CRITICAL] Crawling aborted completely: The system is offline. Check your internet connection.");
//                //stack.clear();
//            } catch (PageHttpException e) {
//                handleHttpError(current, e);
//            } catch (CrawlTimeoutException e) {
//                handleTimeoutError(current, e);
//            } catch (CrawlException e) {
//                handleGenericCrawlError(current, e);
//            }
//        //}
//    }

    public List<URI> process(UrlNode urlNode) throws CrawlException {
        printProgress(urlNode);

        WebPage doc = fetcher.fetch(urlNode.url());

        savePage(doc, urlNode);

        return LinkExtractor.extract(doc, urlNode.url());
    }

    private void savePage(WebPage doc, UrlNode current) {
        String[][] headers = HeaderExtractor.extractHeaders(doc);
        WebsiteData data = new WebsiteData(current.url(), current.depth(), headers);

        boolean isFirstEntry = visitedUrls.size() == 1;
        String[] formattedOutput = OutputFormat.formatLink(data, isFirstEntry);

        storage.writeLines(formattedOutput);
    }

//    private void pushIfValid(URI url, int depth) {
//        if (!validator.isValid(url))return;
//
//        if(depth > maxDepth) {return;}
//
//        //.add is atomic
//        if(visitedUrls.add(url)) {
//            submitNode(new UrlNode(url, depth));
//        }
//    }

//    private boolean isVisited(UrlNode node) {
//        return visitedUrls.contains(node.url);
//    }
//
//    private void markVisited(UrlNode node) {
//        visitedUrls.add(node.url);
//    }

    private void printProgress(UrlNode current) {
        System.out.println("\t".repeat(current.depth() - 1) + "Reading URL: " + current.url() + " | Depth: " + current.depth());
    }

//    private void processAndSaveData(WebPage doc, UrlNode current) {
//        // Extraction
//        String[][] headers = HeaderExtractor.extractHeaders(doc);
//        WebsiteData data = new WebsiteData(current.url, current.depth, headers);
//
//        // Formating and Saving
//        boolean isFirstEntry = visitedUrls.size() == 1;
//        String[] formattedOutput = OutputFormat.formatLink(data, isFirstEntry);
//        storage.writeLines(formattedOutput);
//
//        // Console Output
//        for (String line : formattedOutput) {
//            System.out.println("\t".repeat(current.depth - 1) + line);
//        }
//    }

//    private void enqueueChildLinks(WebPage doc, UrlNode current) {
//        List<URI> extractedLinks = LinkExtractor.extract(doc, current.url);
//        for (URI nextUrl : extractedLinks) {
//            System.out.println(current.url + " -> " + nextUrl);
//            pushIfValid(nextUrl, current.depth + 1);
//        }
//    }
//
//    private void handleBrokenLink(UrlNode current) {
//        String brokenMsg = OutputFormat.formatBrokenLink(current.url, current.depth);
//        storage.writeLine(brokenMsg);
//        System.out.println("\t".repeat(Math.max(0, current.depth - 1)) + brokenMsg);
//    }
//
//    private void handleOfflineError(UrlNode current, OfflineException e) {
//        String errMsg = OutputFormat.formatOfflineError(current.url, current.depth);
//        storage.writeLine(errMsg);
//        System.out.println("\t".repeat(Math.max(0, current.depth - 1)) + errMsg);
//    }
//
//    private void handleHttpError(UrlNode current, PageHttpException e) {
//        String errMsg = OutputFormat.formatHttpError(current.url, current.depth, e.getStatusCode());
//        storage.writeLine(errMsg);
//        System.out.println("\t".repeat(Math.max(0, current.depth - 1)) + errMsg);
//    }
//
//    private void handleTimeoutError(UrlNode current, CrawlTimeoutException e) {
//        String errMsg = OutputFormat.formatTimeoutError(current.url, current.depth);
//        storage.writeLine(errMsg);
//        System.out.println("\t".repeat(Math.max(0, current.depth - 1)) + errMsg);
//    }
//
//    private void handleGenericCrawlError(UrlNode current, CrawlException e) {
//        String errMsg = OutputFormat.formatBrokenLink(current.url, current.depth);
//        storage.writeLine(errMsg + " (Reason: " + e.getMessage() + ")");
//        System.out.println("\t".repeat(Math.max(0, current.depth - 1)) + errMsg);
//    }
}