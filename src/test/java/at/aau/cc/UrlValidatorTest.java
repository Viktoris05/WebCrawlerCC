package at.aau.cc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;


class UrlValidatorTest {
    URI link;
    URI[] domain;

    @BeforeEach
    void setUp() {
        try {
            link = new URI("https://benji.link");
            domain = new URI[]{new URI("https://benji.link")};
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    void changeLink(String link) {
        this.link = URI.create(link);
    }


    @Test
    void isValid() {
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertTrue(result);
    }

    @Test
    void isInvalidBecauseNull() {
        link = null;
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isInvalidBecauseStartsWithMailTo() {
        changeLink("mailto:" + link);
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }

    @Test
    void isInvalidBecauseStartsWithJavaScript() {
        changeLink("javascript:" + link);
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

        Assertions.assertFalse(result);
    }


    @Test
    void isInvalidBecauseNoProtocol() {
        changeLink("benji.link");
        UrlValidator urlValidator = new UrlValidator(domain);

        var result = urlValidator.isValid(link);

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