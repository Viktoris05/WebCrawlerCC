package at.aau.cc;

import java.net.URI;

public interface PageFetcher {
    WebPage fetch(URI url) throws CrawlException;
}
