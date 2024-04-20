package com.bhupender.minerva.service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        // Arrange
        String message = "Resource not found";

        // Act
        NotFoundException exception = new NotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        // Arrange
        String message = "Resource not found";
        Throwable cause = new RuntimeException("Internal error");

        // Act
        NotFoundException exception = new NotFoundException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testResponseStatus() {
        // Arrange
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        // Act
        ResponseStatus responseStatusAnnotation = NotFoundException.class.getAnnotation(ResponseStatus.class);

        // Assert
        assertEquals(expectedStatus, responseStatusAnnotation.value());
    }
}
