package com.guerrero.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RandomIdGeneratorTest {

    @Test
    void testGenerateRandomId_ValidIdLength() {
        // Arrange and Act
        int idLength = 8;
        String randomId = RandomIdGenerator.generateRandomId(idLength);

        // Assert
        assertEquals(idLength, randomId.length());
        assertFalse(randomId.contains("-"));
    }

    @Test
    void testGenerateRandomId_InvalidIdLength() {
        // Arrange
        int invalidIdLength = 33;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> RandomIdGenerator.generateRandomId(invalidIdLength));
    }
}
