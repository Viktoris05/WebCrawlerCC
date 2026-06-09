package at.aau.cc;

import java.net.URI;

public record WebsiteExceptionHolder(URI link, int currentDepth, String errorMessage) implements WebsiteResult {

}
