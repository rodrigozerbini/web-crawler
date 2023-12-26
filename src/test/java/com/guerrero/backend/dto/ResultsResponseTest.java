package com.guerrero.backend.dto;

import com.guerrero.backend.model.Status;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultsResponseTest {

    @Test
    void testConstructor() {
        // Arrange
        String searchId = "search123";
        Status status = Status.active;
        Set<String> urls = new HashSet<>();
        urls.add("http://example.com");

        // Act
        ResultsResponse resultsResponse = new ResultsResponse(searchId, status, urls);

        // Assert
        assertEquals(searchId, resultsResponse.getId());
        assertEquals(status, resultsResponse.getStatus());
        assertEquals(urls, resultsResponse.getUrls());
    }
}

