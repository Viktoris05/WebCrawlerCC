package at.aau.cc;

public interface WebElement {
    String text();
    String tagName();
    String attr(String attributeKey);
}
