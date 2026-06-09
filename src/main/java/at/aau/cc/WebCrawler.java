package at.aau.cc;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WebCrawler {
    private final Set<URI> visitedUrls;
    private final Storage storage;
    private final PageFetcher fetcher;
    private final Map<UrlNode, WebsiteResult> websiteDataMap;
    private final Logger logger = Logger.getLogger(WebCrawler.class.getName());

    public WebCrawler(Storage storage, PageFetcher fetcher) {
        this.storage = storage;
        this.fetcher = fetcher;
        this.visitedUrls = ConcurrentHashMap.newKeySet();
        websiteDataMap = new ConcurrentHashMap<>();
    }


    public List<URI> process(UrlNode urlNode) {
        try{
            WebPage doc = fetcher.fetch(urlNode.url());

            savePage(doc, urlNode);

            return LinkExtractor.extract(doc, urlNode.url());
        } catch (CrawlException e) {
            handleCrawlException(urlNode, e);
        }

        return List.of();
    }

    private void savePage(WebPage doc, UrlNode current) {
        String[][] headers = HeaderExtractor.extractHeaders(doc);
        WebsiteData data = new WebsiteData(current.url(), current.depth(), headers);

        websiteDataMap.put(current, data);
    }

    private void handleCrawlException(UrlNode current, Exception e) {
        String errorMessage = switch(e){
            case OfflineException exception ->
                OutputFormat.formatOfflineError(current.url(),current.depth());
            case CrawlTimeoutException exception ->
                OutputFormat.formatTimeoutError(current.url(), current.depth());
            default ->
                OutputFormat.formatBrokenLink(current.url(), current.depth());
        };
        logger.log(Level.WARNING, errorMessage);


        WebsiteExceptionHolder exceptionData = new WebsiteExceptionHolder(current.url(), current.depth(), errorMessage);
        websiteDataMap.put(current, exceptionData);
    }

    public void writeReport(UrlNode current){
        //remove so each link is shown only once
        switch(websiteDataMap.remove(current)){
            case WebsiteData data -> {
                boolean isFirstEntry = visitedUrls.size() == 1;
                String[] formattedOutput = OutputFormat.formatLink(data, isFirstEntry);

                storage.writeLines(formattedOutput);

                //recursively write all children
                current.children().forEach(this::writeReport);
            }
            case WebsiteExceptionHolder failure -> storage.writeLine(failure.errorMessage());
            case null -> logger.log(Level.FINE, "Found link outside of Domain: {0}", current.url());
            default -> throw new RuntimeException("Unknown WebsiteResult " + websiteDataMap.get(current));
        }
    }
}