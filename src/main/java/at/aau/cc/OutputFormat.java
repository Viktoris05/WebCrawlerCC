package at.aau.cc;

import java.net.URI;

public class OutputFormat {
    public static String[] formatLink(WebsiteData websiteData, boolean firstEntry){
        checkDepth(websiteData.currentDepth());

        String[] output;

        int outputLength = getOutputLength(websiteData);

        output = new String[outputLength];

        if(firstEntry){
            output[0] = writeFirstLine(websiteData.link());
        }else{
            output[0] = writeLink(websiteData.link(), websiteData.currentDepth());
        }

        output[1] = writeCurrentDepth(websiteData.currentDepth());

        for(int i = 2; i < outputLength; i++){
            String header = websiteData.getHeader(i - 2);
            output[i] = writeHeader(header , websiteData.currentDepth(), websiteData.getHeaderTag(i - 2));
        }

        return output;
    }

    public static String formatBrokenLink(URI link, int depth){
        checkDepth(depth);
        return writeBrokenLink(link, depth);
    }

    public static String formatRecurringLink(URI link, int depth){
        checkDepth(depth);
        return writeRecurringLink(link, depth);
    }

    public static String formatLinkOnly(URI link, int depth){
        checkDepth(depth);
        return writeLink(link, depth);
    }


    private static void checkDepth(int depth){
        if(depth < 1){
            throw new IllegalArgumentException("Depth must be greater than zero");
        }
    }


    private static int getOutputLength(WebsiteData websiteData){
        int outputLength = 2; //link + currentDepth
        outputLength += websiteData.headers().length;

        return outputLength;
    }

    private static String writeFirstLine(URI link){
        return "input: <a>" + link + "</a>";
    }

    private static String writeLink(URI link, int depth){
        return "<br>" + "-".repeat(2*(depth-1)) + "> link to <a>" + link + "</a>";
    }

    private static String writeBrokenLink(URI link, int depth){
        return "<br>" + "-".repeat(2*(depth-1)) + "> broken link to <a>" + link + "</a>";
    }

    private static String writeRecurringLink(URI link, int depth){
        return "<br>" + "-".repeat(2*(depth-1)) + "> <a>" + link + "</a>, already visited";
    }

    private static String writeCurrentDepth(int depth){
        return "<br>depth: " + depth;
    }

    private static String writeHeader(String heading, int depth, int headingTag){
        return "#".repeat(headingTag) + " " + "-".repeat(2*(depth-1)) + (((depth)>1) ? "> " : "") + heading;
    }

    public static String formatOfflineError(URI link, int depth) {
        checkDepth(depth);
        return "<br>" + "-".repeat(2 * (depth - 1)) + "> [OFFLINE ERROR] Cannot resolve host or network is down for <a>" + link + "</a>";
    }

    public static String formatHttpError(URI link, int depth, int statusCode) {
        checkDepth(depth);
        return "<br>" + "-".repeat(2 * (depth - 1)) + "> [HTTP ERROR " + statusCode + "] failed to load <a>" + link + "</a>";
    }

    public static String formatTimeoutError(URI link, int depth) {
        checkDepth(depth);
        return "<br>" + "-".repeat(2 * (depth - 1)) + "> [TIMEOUT ERROR] Connection timed out for <a>" + link + "</a>";
    }

}
