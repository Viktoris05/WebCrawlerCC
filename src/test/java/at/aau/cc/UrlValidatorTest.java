package at.aau.cc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertTrue(result);
    }

    @Test
    void isInvalidBecauseEmpty() {
        changeLink("");
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isInvalidBecauseNull(){
        changeLink(null);
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isInvalidBecauseContainsHashtag(){
        changeLink("#" + link);
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isInvalidBecauseStartsWithMailTo(){
        changeLink("mailto:" + link);
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isInvalidBecauseStartsWithJavaScript(){
        changeLink("javascript:" + link);
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isInvalidBecauseContainsSpace() {
        changeLink("https://benji link");
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isValidDomains() {
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValidDomains();

        Assertions.assertTrue(result);
    }

    @Test
    void isInvalidDomainsBecauseEmpty() {
        String[] emptyDomain = new String[]{""};
        changeDomain(emptyDomain);
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValidDomains();

        Assertions.assertFalse(result);
    }


    @Test
    void containedWithinDomains() {
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.containedWithinDomains(link);

        Assertions.assertTrue(result);
    }

    @Test
    void notContainedWithinDomains() {
        changeLink("https://github.com");
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.containedWithinDomains(link);

        Assertions.assertFalse(result);
    }
}