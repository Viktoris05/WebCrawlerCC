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
        pushIfValid(startUrl, 1);

        while (!stack.isEmpty()) {
            process(stack.pop());
        }

    }

    public void process(UrlNode current){
        if (!isVisited(current)) {
            markVisited(current);

            printProgress(current);
            try {
                // HTTP Fetching
                Document doc = Jsoup.connect(current.url).get();

                processAndSaveData(doc, current);

                if (current.depth < maxDepth) {
                    enqueueChildLinks(doc, current);
                }

            } catch (Exception e) {
                handleBrokenLink(current);
            }
        }
    }

    private void pushIfValid(String url, int depth) {
        if (validator.isValid(url) && !visitedUrls.contains(url)) {
            stack.push(new UrlNode(url, depth));
        }
    }

    private boolean isVisited(UrlNode node) {
        return visitedUrls.contains(node.url);
    }

    private void markVisited(UrlNode node) {
        visitedUrls.add(node.url);
    }

    private void printProgress(UrlNode current) {
        System.out.println("\t".repeat(current.depth - 1) + "Reading URL: " + current.url + " | Depth: " + current.depth);
    }

    private void processAndSaveData(Document doc, UrlNode current) {
        // Extraction
        String[][] headers = HeaderExtractor.extractHeaders(doc);
        WebsiteData data = new WebsiteData(current.url, current.depth, headers);

        // Formating and Saving
        boolean isFirstEntry = visitedUrls.size() == 1;
        String[] formattedOutput = OutputFormat.formatLink(data, isFirstEntry);
        storage.writeLines(formattedOutput);

        // Console Output
        for (String line : formattedOutput) {
            System.out.println("\t".repeat(current.depth - 1) + line);
        }
    }

    private void enqueueChildLinks(Document doc, UrlNode current) {
        List<String> extractedLinks = LinkExtractor.extract(doc, current.url);
        for (String nextUrl : extractedLinks) {
            pushIfValid(nextUrl, current.depth + 1);
        }
    }

    private void handleBrokenLink(UrlNode current) {
        String brokenMsg = OutputFormat.formatBrokenLink(current.url, current.depth);
        storage.writeLine(brokenMsg);
        System.out.println("\t".repeat(Math.max(0, current.depth - 1)) + brokenMsg);
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