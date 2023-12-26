package com.guerrero.backend.dto;

/**
 * Represents a response object for a crawling POST request.
 * Contains the unique identifier associated with the search.
 */
public class CrawlResponse {

    private final String id;

    /**
     * Constructs a CrawlResponse object with the provided identifier.
     * @param id The unique identifier associated with the search.
     */
    public CrawlResponse(String id) {
        this.id = id;
    }

    /**
     * Gets the identifier associated with the search.
     * @return The unique identifier of the search.
     */
    public String getId() {
        return id;
    }
}
