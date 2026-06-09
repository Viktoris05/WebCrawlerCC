package at.aau.cc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

// Inner class to link a URL string with its current depth level
public record UrlNode(URI url, int depth, UrlNode parent, List<UrlNode> children) {
    public UrlNode(URI url, int depth, UrlNode parent) {
        this(url, depth, parent, new ArrayList<>());
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public static UrlNode createRootNode(URI url) {
        return new UrlNode(url, 1, null);
    }

    public UrlNode createChildNode(URI url) {
        return new UrlNode(url, depth + 1, this);
    }

    @Override
    public boolean equals(Object obj) {
        //pattern matching -> typecast into UrlNode
        return obj instanceof UrlNode other && url.equals(other.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
