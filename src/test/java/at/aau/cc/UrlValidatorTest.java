package at.aau.cc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {
    String link;
    String[] domain;

    @BeforeEach
    void setUp() {
        link = "https://benji.link";
        domain = new String[]{"https://benji.link"};
    }

    void changeLink(String link) {
        this.link = link;
    }
    void changeDomain(String[] domain) {
        this.domain = domain;
    }

    @Test
    void isValid() {
        UrlValidator urlValidater = new UrlValidator(domain);

        var result = urlValidater.isValid(link);

        Assertions.assertTrue(result);
    }

    @Test
    void isInvalidBecauseEmpty() {
        changeLink("");
        UrlValidator urlValidater = new UrlValidator(domain);

        var result = urlValidater.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isInvalidBecauseContainsSpace() {
        changeLink("https://benji link");
        UrlValidator urlValidater = new UrlValidator(domain);

        var result = urlValidater.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isValidDomains() {
        UrlValidator urlValidater = new UrlValidator(domain);

        var result = urlValidater.isValidDomains();

        Assertions.assertTrue(result);
    }

    @Test
    void isInvalidDomainsBecauseEmpty() {
        String[] emptyDomain = new String[]{""};
        changeDomain(emptyDomain);
        UrlValidator urlValidater = new UrlValidator(domain);

        var result = urlValidater.isValidDomains();

        Assertions.assertFalse(result);
    }


    @Test
    void containedWithinDomains() {
        UrlValidator urlValidater = new UrlValidator(domain);

        var result = urlValidater.containedWithinDomains(link);

        Assertions.assertTrue(result);
    }

    @Test
    void notContainedWithinDomains() {
        changeLink("https://github.com");
        UrlValidator urlValidater = new UrlValidator(domain);

        var result = urlValidater.containedWithinDomains(link);

        Assertions.assertFalse(result);
    }
}