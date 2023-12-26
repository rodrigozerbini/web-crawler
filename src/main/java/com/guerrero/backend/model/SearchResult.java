package com.guerrero.backend.model;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Represents the result of a search operation.
 * The result includes a unique identifier, a status, and a set of URLs.
 */
public class SearchResult {

    /**
     * The unique identifier for this search result.
     */
    String id;

    /**
     * The status of the search result, indicating whether the search is active or done.
     */
    Status status;

    /**
     * The set of URLs associated with this search result.
     */
    private Set<String> urls;

    /**
     * Logger instance for logging messages related to this class.
     */
    final Logger logger = LoggerFactory.getLogger(SearchResult.class);

    /**
     * Constructs a new SearchResult instance with the specified identifier.
     * The status is initially set to active, and the URLs set is initialized.
     * @param id the unique identifier for the search result
     */
    public SearchResult(String id) {
        this.id = id;
        this.status = Status.active;
        this.urls = new HashSet<String>();
    }

    /**
     * Adds a URL to the set of URLs associated with this search result.
     * @param url the URL to add
     */
    public void addUrl(String url) {
        urls.add(url);
    }

    /**
     * Gets the unique identifier for this search result.
     * @return the unique identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the status of this search result.
     * @return the status (active or done)
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets the set of URLs associated with this search result.
     * @return the set of URLs
     */
    public Set<String> getUrls() {
        return urls;
    }

    /**
     * Sets the status of this search result to the specified status.
     * @param status the new status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
