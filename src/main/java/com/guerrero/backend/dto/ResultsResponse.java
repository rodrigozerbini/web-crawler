package com.guerrero.backend.dto;

import com.guerrero.backend.model.Status;

import java.util.Set;

/**
 * Represents a response object containing search results.
 * Contains the unique identifier, status, and set of URLs associated with the search result.
 */
public class ResultsResponse {

    String id;
    Status status;
    Set<String> urls;

    /**
     * Constructs a ResultsResponse object with the provided identifier, status, and set of URLs.
     * @param id     The unique identifier associated with the search result.
     * @param status The status of the search result (active or done).
     * @param urls   The set of URLs associated with the search result.
     */
    public ResultsResponse(String id, Status status, Set<String> urls) {
        this.id = id;
        this.status = status;
        this.urls =urls;
    }

    /**
     * Gets the unique identifier associated with the search result.
     * @return The unique identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the status of the search result (active or done).
     * @return The status of the search result.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets the set of URLs associated with the search result.
     * @return The set of URLs.
     */
    public Set<String> getUrls() {
        return urls;
    }
}
