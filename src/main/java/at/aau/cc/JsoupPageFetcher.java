package at.aau.cc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class JsoupPageFetcher implements PageFetcher {
    @Override
    public WebPage fetch(URI url) throws CrawlException {
        try {
            // Configuring an explicit timeout to ensure the app doesn't hang indefinitely
            Document doc = Jsoup.connect(String.valueOf(url)).timeout(5000).get();
            return new JsoupWebPage(doc);
        } catch (org.jsoup.HttpStatusException e) {
            // Catching standard HTTP errors like 404 or 500
            throw new PageHttpException(e.getStatusCode(), "HTTP error fetching URL: status " + e.getStatusCode(), e);
        } catch (java.net.SocketTimeoutException e) {
            throw new CrawlTimeoutException("Timeout occurred while connecting to URL", e);
        } catch (java.net.UnknownHostException | java.net.ConnectException e) {
            throw new OfflineException("Network is unreachable or host is unknown. The system might be offline.", e);
        } catch (java.io.IOException e) {
            throw new CrawlException("General I/O error occurred while fetching URL", e);
        }
    }

    private static class JsoupWebPage implements WebPage {
        private final Document doc;

        JsoupWebPage(Document doc) {
            this.doc = doc;
        }

        @Override
        public List<WebElement> select(String cssQuery) {
            List<WebElement> elements = new ArrayList<>();
            for (Element el : doc.select(cssQuery)) {
                elements.add(new JsoupWebElement(el));
            }
            return elements;
        }
    }

    private static class JsoupWebElement implements WebElement {
        private final Element el;

        JsoupWebElement(Element el) {
            this.el = el;
        }

        @Override
        public String text() {
            return el.text();
        }

        @Override
        public String tagName() {
            return el.tagName();
        }

        @Override
        public String attr(String attributeKey) {
            return el.attr(attributeKey);
        }
    }
}