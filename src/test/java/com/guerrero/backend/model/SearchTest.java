package com.guerrero.backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    @Test
    void testConstructor_ValidKeyword() {
        // Arrange
        String validKeyword = "Alice";

        // Act
        Search search = new Search(validKeyword);

        // Assert
        assertEquals(validKeyword.toLowerCase(), search.getKeyword());
    }

    @Test
    void testConstructor_InvalidKeyword() {
        // Arrange
        String invalidKeyword = "key";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> new Search(invalidKeyword));
    }

    @Test
    void testGetKeyword() {
        // Arrange
        String keyword = "security";
        Search search = new Search(keyword);

        // Act
        String result = search.getKeyword();

        // Assert
        assertEquals(keyword.toLowerCase(), result);
    }

    @Test
    void testGetId() {
        // Arrange
        String keyword = "command";
        int expectedIdLength = 8;
        Search search = new Search(keyword);

        // Act
        String id = search.getId();

        // Assert
        assertFalse(id.isEmpty());
        assertEquals(expectedIdLength, search.getId().length());
    }
}
