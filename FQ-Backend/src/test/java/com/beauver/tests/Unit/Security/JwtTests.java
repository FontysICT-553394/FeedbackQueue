package com.beauver.tests.Unit.Security;

import com.beauver.Security.JwtUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class JwtTests {

    @Inject
    JwtUtil jwtUtil;

    @ParameterizedTest
    @CsvSource({
            "12345",
            "67890",
            "999"
    })
    public void testGenerateToken_Success(String userId) {
        // Act
        String token = jwtUtil.generateToken(userId);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "12345",
            "67890",
            "999",
            "Test"
    })
    public void testGetIdFromToken_Success(String userId) {
        // Arrange
        String token = jwtUtil.generateToken(userId);

        // Act
        String extractedId = jwtUtil.getIdFromToken(token);

        // Assert
        assertEquals(userId, extractedId);
    }

    @Test
    public void testValidateToken_Success() {
        // Arrange
        String token = jwtUtil.generateToken("12345");

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @ParameterizedTest
    @CsvSource({
            "invalid.token.here",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid.signature",
            "''"
    })
    public void testValidateToken_Fail_InvalidToken(String invalidToken) {
        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testGetIdFromToken_Fail_InvalidToken() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            jwtUtil.getIdFromToken("invalid.token.string");
        });
    }
}