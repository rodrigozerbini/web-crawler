package com.guerrero.backend.dto;

/**
 * Represents a request object for initiating a search operation.
 * Contains the keyword to be searched.
 */
public class SearchRequest {

    private String keyword;

    /**
     * Gets the keyword for the search.
     * @return The keyword to be searched.
     */
    public String getKeyword() {
        return keyword;
    }
}
