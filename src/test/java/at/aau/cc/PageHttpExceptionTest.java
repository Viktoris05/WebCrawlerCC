package at.aau.cc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageHttpExceptionTest {

    @Test
    public void testExceptionRetainsStatusCode() {
        // Arrange
        int expectedCode = 404;
        String expectedMessage = "Page not found";
        Throwable cause = new RuntimeException("Underlying cause");

        // Act
        PageHttpException exception = new PageHttpException(expectedCode, expectedMessage, cause);

        // Assert
        assertEquals(expectedCode, exception.getStatusCode(), "Status code should match the injected value");
        assertEquals(expectedMessage, exception.getMessage(), "Message should match");
        assertEquals(cause, exception.getCause(), "Cause should match");
    }
}