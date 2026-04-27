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

    // Stack to keep the URLs
    private final Stack<UrlNode> stack;

    public WebCrawler(int maxDepth, String[] domains) {
        this.validator = new UrlValidator(domains);
        this.maxDepth = maxDepth;
        this.visitedUrls = new HashSet<>();
        this.stack = new Stack<>();
    }

    public void start(String startUrl) {
        // Push the starting URL onto the stack with an initial depth of 1
        stack.push(new UrlNode(startUrl, 1));

        // Loop continuously as long as there are URLs left in the stack
        while (!stack.isEmpty()) {
            UrlNode current = stack.pop();

            if (visitedUrls.contains(current.url)) {
                continue; // Skip it and move to the next URL in the stack
            }

            visitedUrls.add(current.url);

            // Console output for visual tracking
            System.out.println("\t".repeat(current.depth-1) + "Reading URL: " + current.url + " | Depth: " + current.depth);

            try {
                // Check if the link is "alive" and download the document
                Document doc = Jsoup.connect(current.url).get();
                String[][] headers = HeaderExtractor.extractHeaders(doc);

                WebsiteData data = new WebsiteData(current.url, current.depth, headers);

                //Format output via OutputFormat and save via MarkdownStorage
                boolean isFirstEntry = visitedUrls.size() == 1;
                String[] formattedOutput = OutputFormat.formatLink(data, isFirstEntry);

                for (String line : formattedOutput) {
                    System.out.println("\t".repeat(current.depth - 1) + line);
                }

                if (current.depth < maxDepth) {

                    List<String> extractedLinks = LinkExtractor.extract(doc, current.url);

                    // Check everything and add to stack
                    for (String nextUrl : extractedLinks) {
                        if (validator.isValid(nextUrl) && !visitedUrls.contains(nextUrl)) {
                            stack.push(new UrlNode(nextUrl, current.depth + 1));
                        }
                    }
                }

            } catch (Exception e) {
                // Handling broken links
                String brokenMsg = OutputFormat.formatBrokenLink(current.url, current.depth);
                System.out.println("\t".repeat(Math.max(0, current.depth - 1)) + brokenMsg);
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