package com.guerrero.backend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CrawlResponseTest {

    @Test
    void testConstructor() {
        // Arrange
        String searchId = "search123";

        // Act
        CrawlResponse crawlResponse = new CrawlResponse(searchId);

        // Assert
        assertEquals(searchId, crawlResponse.getId());
    }
}