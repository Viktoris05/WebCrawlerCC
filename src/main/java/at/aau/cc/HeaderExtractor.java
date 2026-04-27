package at.aau.cc;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HeaderExtractor {

    // Gets full Document and returns all headers
    public static String[][] extractHeaders(Document doc) {
        Elements headerElements = doc.select("h1, h2, h3, h4, h5, h6");
        String[][] headers = new String[headerElements.size()][2];

        for (int i = 0; i < headerElements.size(); i++) {
            Element header = headerElements.get(i);
            headers[i][0] = header.text();
            headers[i][1] = header.tagName().substring(1);
        }

        return headers;
    }
}