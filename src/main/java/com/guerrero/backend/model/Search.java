package com.guerrero.backend.model;

import com.guerrero.backend.util.RandomIdGenerator;

/**
 * Represents a search operation.
 * The search is identified by a keyword and has a unique identifier.
 */
public class Search {

    /**
     * The keyword associated with this search.
     */
    String keyword;

    /**
     * The unique identifier for this search.
     */
    String id;

    /**
     * Minimum length allowed for a valid keyword.
     */
    private static final int MIN_KEYWORD_LENGTH = 4;

    /**
     * Maximum length allowed for a valid keyword.
     */
    private static final int MAX_KEYWORD_LENGTH = 32;

    /**
     * Constructs a new Search instance with the specified keyword.
     *
     * @param keyword the keyword for the search
     * @throws IllegalArgumentException if the keyword length is not between 4 and 32 characters
     */
    public Search(String keyword) {
        if (!isValidKeyword(keyword)) {
            throw new IllegalArgumentException("Keyword length must be between 4 and 32 characters.");
        }
        int idLength = 8;
        this.id = RandomIdGenerator.generateRandomId(idLength);
        this.keyword = keyword.toLowerCase();
    }

    /**
     * Gets the keyword associated with this search.
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Gets the unique identifier for this search.
     * @return the unique identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Validates if the given keyword has a length within the allowed range.
     * @param keyword the keyword to validate
     * @return true if the keyword is valid, false otherwise
     */
    private static boolean isValidKeyword(String keyword) {
        int length = keyword.length();
        return length >= MIN_KEYWORD_LENGTH && length <= MAX_KEYWORD_LENGTH;
    }
}