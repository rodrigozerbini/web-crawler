package com.guerrero.backend.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SearchResultTest {

    @Test
    void testConstructor() {
        // Arrange
        String searchId = "search123";

        // Act
        SearchResult searchResult = new SearchResult(searchId);

        // Assert
        assertEquals(searchId, searchResult.getId());
        assertEquals(Status.active, searchResult.getStatus());
        assertTrue(searchResult.getUrls().isEmpty());
    }

    @Test
    void testAddUrl() {
        // Arrange
        SearchResult searchResult = new SearchResult("search123");
        String urlToAdd = "http://example.com";
        int expectedListSize = 1;

        // Act
        searchResult.addUrl(urlToAdd);

        // Assert
        assertTrue(searchResult.getUrls().contains(urlToAdd));
        assertEquals(expectedListSize, searchResult.getUrls().size());
    }

    @Test
    void testGetId() {
        // Arrange
        String expectedSearchId = "search123";
        SearchResult searchResult = new SearchResult(expectedSearchId);

        // Act
        String searchId = searchResult.getId();

        // Assert
        assertEquals(expectedSearchId, searchId);
    }

    @Test
    void testGetStatus() {
        // Arrange
        SearchResult searchResult = new SearchResult("search123");

        // Act
        Status status = searchResult.getStatus();

        // Assert
        assertEquals(Status.active, status);
    }

    @Test
    void testSetStatus() {
        // Arrange
        SearchResult searchResult = new SearchResult("search123");

        // Act
        searchResult.setStatus(Status.done);

        // Assert
        assertEquals(Status.done, searchResult.getStatus());
    }

    @Test
    void testGetUrls() {
        // Arrange
        SearchResult searchResult = new SearchResult("search123");
        String urlToAdd = "http://example.com";
        int expectedSetSize = 1;
        searchResult.addUrl(urlToAdd);


        // Act
        Set<String> urls = searchResult.getUrls();

        // Assert
        assertEquals(expectedSetSize, urls.size());
        assertTrue(urls.contains(urlToAdd));
    }
}
