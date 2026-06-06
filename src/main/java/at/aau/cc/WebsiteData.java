package at.aau.cc;

import java.net.URI;

public record WebsiteData(URI link, int currentDepth, String[][] headers) {
    public String getHeader(int index) {
        return headers[index][0];
    }

    public int getHeaderTag(int index) {
        return Integer.parseInt(headers[index][1]);
    }
}
