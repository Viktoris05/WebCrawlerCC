package at.aau.cc;

import java.util.List;

public class HeaderExtractor {

    public static String[][] extractHeaders(WebPage doc) {
        List<WebElement> headerElements = doc.select("h1, h2, h3, h4, h5, h6");
        String[][] headers = new String[headerElements.size()][2];

        for (int i = 0; i < headerElements.size(); i++) {
            WebElement header = headerElements.get(i);
            headers[i][0] = header.text();
            headers[i][1] = header.tagName().substring(1);
        }

        return headers;
    }
}