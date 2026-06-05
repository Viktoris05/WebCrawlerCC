package at.aau.cc;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkExtractor {

    public static List<String> extract(Document doc, String currentUrl) {
        List<String> foundUrls = new ArrayList<>();

        // Find all <a> tags that contain a href attribute
        Elements htmlUrls = doc.select("a[href]");
        for (Element element : htmlUrls) {
            foundUrls.add(element.attr("abs:href"));
        }

        // Search in buttons
        Elements buttons = doc.select("button[onclick]");

        // Making a pattern that searches for location.href = '' or ""

        // \\s*=\\s* to check for spaces (href='' and href = '')
        // ['\"] - searches for '' or ""
        // ([^'\"]+)  connects everything in the link
        Pattern pattern = Pattern.compile("location\\.href\\s*=\\s*['\"]([^'\"]+)['\"]");

        for (Element button : buttons) {
            String onClickVal = button.attr("onclick");
            Matcher matcher = pattern.matcher(onClickVal);

            if (matcher.find()) {
                // group(1) - is anything in '' or ""
                String extractedUrl = matcher.group(1);

                // if the link is not inside, like /main.html
                if (!extractedUrl.startsWith("http")) {
                    try {
                        // Making the url absolute
                        extractedUrl = URI.create(currentUrl).resolve(extractedUrl).toString();
                    } catch (Exception e) {
                        foundUrls.add(extractedUrl);
                        continue;
                    }
                }
                foundUrls.add(extractedUrl);
            }
        }

        return foundUrls;
    }
}