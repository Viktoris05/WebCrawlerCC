package at.aau.cc;

public interface PageFetcher {
    WebPage fetch(String url) throws CrawlException;
}
