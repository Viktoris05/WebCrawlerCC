package at.aau.cc;

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

    public static String formatBrokenLink(String link, int depth){
        checkDepth(depth);
        return writeBrokenLink(link, depth);
    }

    public static String formatRecurringLink(String link, int depth){
        checkDepth(depth);
        return writeRecurringLink(link, depth);
    }

    public static String formatLinkOnly(String link, int depth){
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

    private static String writeFirstLine(String link){
        return "input: <a>" + link + "</a>";
    }

    private static String writeLink(String link, int depth){
        return "<br>" + "-".repeat(2*(depth-1)) + "> link to <a>" + link + "</a>";
    }

    private static String writeBrokenLink(String link, int depth){
        return "<br>" + "-".repeat(2*(depth-1)) + "> broken link to <a>" + link + "</a>";
    }

    private static String writeRecurringLink(String link, int depth){
        return "<br>" + "-".repeat(2*(depth-1)) + "> <a>" + link + "</a>, already visited";
    }

    private static String writeCurrentDepth(int depth){
        return "<br>depth: " + depth;
    }

    private static String writeHeader(String heading, int depth, int headingTag){
        return "#".repeat(headingTag) + " " + "-".repeat(2*(depth-1)) + (((depth)>1) ? "> " : "") + heading;
    }

}
