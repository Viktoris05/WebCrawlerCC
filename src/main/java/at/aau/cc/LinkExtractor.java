package at.aau.cc;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        // Making a pattern that searches for location.href = '' or ""

        // \\s*=\\s* to check for spaces (href='' and href = '')
        // ['\"] - searches for '' or ""
        // ([^'\"]+)  connects everything in the link
        Pattern pattern = Pattern.compile("location\\.href\\s*=\\s*['\"]([^'\"]+)['\"]");

        for (WebElement button : buttons) {
            String onClickVal = button.attr("onclick");
            Matcher matcher = pattern.matcher(onClickVal);

            if (matcher.find()) {
                // group(1) - is anything in '' or ""
                URI extractedUrl = currentUrl.resolve(matcher.group(1));

                foundUrls.add(extractedUrl);
            }
        }

        return foundUrls;
    }
}