package at.aau.cc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class WebCrawler {
    private final int maxDepth;
    private final UrlValidator validator;
    private final Set<String> visitedUrls;
    private final Stack<UrlNode> stack;
    private final MarkdownStorage storage;

    public WebCrawler(int maxDepth, String[] domains, String outputFile) {
        this.validator = new UrlValidator(domains);
        this.maxDepth = maxDepth;
        this.visitedUrls = new HashSet<>();
        this.stack = new Stack<>();
        this.storage = new MarkdownStorage(outputFile);
    }

    public void start(String startUrl) {
        if (validator.isValid(startUrl)) stack.push(new UrlNode(startUrl, 1));

        while (!stack.isEmpty()) {
            UrlNode current = stack.pop();

            if (visitedUrls.contains(current.url)) {
                continue;
            }

            visitedUrls.add(current.url);

            // Console output for visual tracking
            System.out.println("\t".repeat(current.depth-1) + "Reading URL: " + current.url + " | Depth: " + current.depth);

            try {
                // Check if the link is "alive" and download the document
                Document doc = Jsoup.connect(current.url).get();
                String[][] headers = HeaderExtractor.extractHeaders(doc);

                WebsiteData data = new WebsiteData(current.url, current.depth, headers);

                boolean isFirstEntry = visitedUrls.size() == 1;
                String[] formattedOutput = OutputFormat.formatLink(data, isFirstEntry);

                storage.writeLines(formattedOutput);


                if (current.depth < maxDepth) {
                    List<String> extractedLinks = LinkExtractor.extract(doc, current.url);

                    for (String nextUrl : extractedLinks) {
                        if (validator.isValid(nextUrl) && !visitedUrls.contains(nextUrl)) {
                            stack.push(new UrlNode(nextUrl, current.depth + 1));
                        }
                    }
                }

            } catch (Exception e) {
                String brokenMsg = OutputFormat.formatBrokenLink(current.url, current.depth);
                storage.writeLine(brokenMsg);
            }
        }
    }


    // Inner class to link a URL string with its current depth level
    private static class UrlNode {
        String url;
        int depth;

        UrlNode(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }
    }
}