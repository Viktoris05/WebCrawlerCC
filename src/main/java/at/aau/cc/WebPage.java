package at.aau.cc;

import java.util.List;

public interface WebPage {
    List<WebElement> select(String cssQuery);
}
