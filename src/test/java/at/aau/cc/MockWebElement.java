package at.aau.cc;

record MockWebElement(String text, String tagName, String href, String onclick) implements WebElement {
    @Override
    public String attr(String attributeKey) {
        return switch (attributeKey) {
            case "abs:href" -> href;
            case "onclick" -> onclick;
            default -> null;
        };
    }
}
