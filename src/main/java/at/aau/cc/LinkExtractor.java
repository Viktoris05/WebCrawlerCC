package at.aau.cc;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LinkExtractor {

    public static List<URI> extract(WebPage doc, URI currentUrl) {
        List<URI> foundUrls = new ArrayList<>();

        List<WebElement> anchorTags = doc.select("a[href]");
        for (WebElement element : anchorTags) {
            try {
                foundUrls.add(new URI(element.attr("abs:href")));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        // Extract URLs from interactive buttons using JavaScript redirects
        // Modern websites often use button components for navigation instead of standard anchor tags.
        List<WebElement> buttons = doc.select("button[onclick]");

        for (WebElement button : buttons) {
            String onClickVal = button.attr("onclick");

            URI extractedUrl = currentUrl.resolve(onClickVal);

            foundUrls.add(extractedUrl);
        }

        return foundUrls;
    }
}