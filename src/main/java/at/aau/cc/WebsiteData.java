package at.aau.cc;

public record WebsiteData(String link, int currentDepth, String[][] headers) {
    public String getHeader(int index) {
        return headers[index][0];
    }

    public int getHeaderTag(int header) {
        return Integer.parseInt(headers[header][1]);
    }
}
