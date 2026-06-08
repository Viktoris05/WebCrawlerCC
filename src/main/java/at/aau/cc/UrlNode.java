package at.aau.cc;

import java.net.URI;

// Inner class to link a URL string with its current depth level
public record UrlNode(URI url, int depth) {

}
