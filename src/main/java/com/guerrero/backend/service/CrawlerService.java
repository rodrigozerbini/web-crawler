package com.guerrero.backend.service;

import com.guerrero.backend.model.Search;
import com.guerrero.backend.model.SearchResult;
import com.guerrero.backend.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages the crawling process for a specific base URL.
 * This class handles the initiation of search data structures,
 * fetching HTML content from URLs, extracting links, and searching
 * for a specified keyword within the HTML content.
 */
public class CrawlerService {

    /**
     * The base URL for the crawling process.
     */
    private String baseUrl;

    /**
     * Map to store searches by their IDs.
     */
    private Map<String, Search> searches = new HashMap<String, Search>();

    /**
     * Map to store search results by their IDs.
     */
    private Map<String, SearchResult> searchResults = new HashMap<String, SearchResult>();

    /**
     * Map to store URL queues by search IDs.
     * For each search, the crawler will navigate its URLs queue until the queue is empty.
     */
    private Map<String, Queue<String>> urlQueues = new HashMap<String, Queue<String>>();

    /**
     * Map to store found URLs by search IDs.
     * For each search, the foundUrls list is useful for knowing
     * which URLs have already been added to the queue before.
     */
    private Map<String, List<String>> foundUrlsMap = new HashMap<String, List<String>>();

    /**
     * String constants used for URL cleaning
     * and URL extraction from HTML content.
     */
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String CLEANING_REGEX = "/\\.\\./";
    private static final String SLASH = "/";
    private static final String ANCHOR_PATTERN = "<a\\s+[^>]*href\\s*=\\s*\"([^\"]+)\"[^>]*>";

    /**
     * Logger for messages related to the crawling process.
     */
    static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    /**
     * Constructs a CrawlerService instance with the specified base URL.
     * @param baseURL The base URL for the crawling process.
     */
    public CrawlerService(String baseURL) {
        baseUrl = baseURL;
    }

    /**
     * Initiates data structures for a new search based on the provided keyword.
     * @param keyword The keyword for the new search.
     * @return The ID of the initiated search.
     * @throws IllegalArgumentException If the keyword length is invalid.
     */
    public String initializeDataStructures(String keyword) throws IllegalArgumentException {
        Search search = createSearch(keyword);
        SearchResult searchResult = createSearchResult(search);
        Queue<String> urlQueue = createUrlQueue();
        List<String> foundUrls = createFoundUrls();
        saveDataStructures(search, searchResult, urlQueue, foundUrls);
        return search.getId();
    }

    /**
     * Saves the data structures in their respective maps.
     * @param search the search object to be saved.
     * @param searchResult the searchResult object to be saved.
     * @param urlQueue the urlQueue to be saved.
     * @param foundUrls the foundUrls list to be saved.
     */
    private void saveDataStructures(Search search, SearchResult searchResult, Queue<String> urlQueue, List<String> foundUrls) {
        String searchId = search.getId();
        searches.put(searchId, search);
        searchResults.put(searchId, searchResult);
        urlQueues.put(searchId, urlQueue);
        foundUrlsMap.put(searchId, foundUrls);
    }

    /**
     * Creates a list of found URLs containing the baseUrl.
     * @return the list of found URLs.
     */
    private List<String> createFoundUrls() {
        List<String> foundUrls = new ArrayList<>();
        foundUrls.add(baseUrl);
        return foundUrls;
    }

    /**
     * Creates a queue of URLs containing the baseUrl.
     * @return the queue of URLs to be processed.
     */
    private Queue<String> createUrlQueue() {
        Queue<String> urlQueue = new LinkedList<>();
        urlQueue.add(baseUrl);
        return urlQueue;
    }

    /**
     * Creates a SearchResult for the provided search.
     * @return A SearchResult containing an id, status and a list of URLs.
     */
    private SearchResult createSearchResult(Search search) {
        String searchId = search.getId();
        return new SearchResult(searchId);
    }

    /**
     * Retrieves a SearchResult based on a search id.
     * @param searchId The identification of the search.
     * @return A SearchResult containing an id, status and a list of URLs.
     */
    public SearchResult getSearchResult(String searchId) {
        return searchResults.get(searchId);
    }

    /**
     * Initiates the crawling process for the search identified by the provided search ID.
     * This method fetches HTML content from URLs in the search's URL queue, searches for additional
     * URLs within the HTML content, and checks for the presence of the specified keyword.
     * If the keyword is found, the corresponding URL is added to the search result.
     * @param searchId The ID of the active search for which crawling is initiated.
     */
    public void crawl(String searchId) {
        String keyword = searches.get(searchId).getKeyword();
        logger.info("Starting search for keyword: {}, id: {}", keyword, searchId);
        boolean keywordIsFound;
        Queue<String> urlQueue = urlQueues.get(searchId);
        while (!urlQueue.isEmpty()) {
            String currentURL = urlQueue.remove();
            String rawHTML = fetchHTML(currentURL);
            searchForUrlsOnHTML(searchId, rawHTML);
            keywordIsFound = htmlContainsKeyword(keyword, rawHTML);
            if (keywordIsFound) {
                SearchResult searchResult = searchResults.get(searchId);
                searchResult.addUrl(currentURL);
            }
        }
        setSearchStatusToDone(searchId);
        logger.info("# End of search for keyword: {}, id: {}", keyword, searchId);
    }

    /**
     * Sets the status of a search to done.
     * @param searchId the id of the search to be modified.
     */
    private void setSearchStatusToDone(String searchId) {
        SearchResult searchResult = searchResults.get(searchId);
        searchResult.setStatus(Status.done);
    }

    /**
     * Fetches HTML content from the specified URL.
     * @param url The URL from which to fetch HTML content.
     * @return The raw HTML content as a string.
     */
    private String fetchHTML(String url) {
        StringBuilder rawHTML = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            BufferedReader input = new BufferedReader(new InputStreamReader(urlObject.openStream()));
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                rawHTML.append(inputLine);
            }
            input.close();
        } catch (Exception e) {
            logger.error("Error in fetching HTML from URL: {}", url);
            logger.error("Exception: {}", e.toString());
        }
        return rawHTML.toString();
    }

    /**
     * Searches for URLs within the HTML content and updates the URL queue and found URLs list.
     * @param searchId The ID of the active search.
     * @param rawHTML  The raw HTML content to process.
     */
    private void searchForUrlsOnHTML(String searchId, String rawHTML) {
        Pattern pattern = Pattern.compile(ANCHOR_PATTERN);
        Matcher matcher = pattern.matcher(rawHTML);
        List<String> foundUrls = foundUrlsMap.get(searchId);
        Queue<String> urlQueue = urlQueues.get(searchId);

        while (matcher.find()) {
            String foundURL = matcher.group(1);
            foundURL = appendBaseUrlIfNeeded(foundURL);

            if (hasSameBaseURL(foundURL) && !foundUrls.contains(foundURL)) {
                urlQueue.add(foundURL);
                foundUrls.add(foundURL);
            }
        }
    }

    /**
     * Appends the base URL to the provided URL if needed and resolves any relative paths.
     * @param url The URL to be cleaned and resolved.
     * @return The cleaned and resolved URL.
     */
    private String appendBaseUrlIfNeeded(String url) {
        try {
            if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
                URL baseUrl = new URL(this.baseUrl);
                URL resolvedUrl = new URL(baseUrl, url);
                String cleanedUrl = resolvedUrl.toString();

                // Remove unnecessary components like "../"
                cleanedUrl = cleanedUrl.replaceAll(CLEANING_REGEX, SLASH);
                return cleanedUrl;
            }
        } catch (MalformedURLException e) {
            logger.error("Error in cleaning URL {}: {}", url, e.toString());
        }
        // Return the original URL if there's an issue with parsing or if it starts with "http" or "https"
        return url;
    }

    /**
     * Checks if the provided URL has the same base as the baseUrl.
     * @param url the URL to be checked.
     * @return true if the provided URL has the same base as the baseUrl, false otherwise.
     */
    private boolean hasSameBaseURL(String url) {
        return url.startsWith(baseUrl);
    }

    /**
     * Checks if the provided HTML contains the provided keyword.
     * @param keyword the keyword to be checked.
     * @param rawHTML the raw HTML content.
     * @return true if the HTML contains the keyword, false otherwise.
     */
    public boolean htmlContainsKeyword(String keyword, String rawHTML) {
        // Case-insensitive search for the keyword with word boundaries
        String pattern = "(?i).*\\b" + keyword + "\\b.*";
        return rawHTML.matches(pattern);
    }

    /**
     * Creates a Search object with the provided keyword.
     * @param keyword The keyword to be searched.
     * @return A search object with an id and a keyword.
     */
    Search createSearch(String keyword) {
        return new Search(keyword);
    }

    /**
     * Gets the searches map.
     * @return The searches map.
     */
    public Map<String, Search> getSearches() {
        return searches;
    }

    /**
     * Gets the searchResults map.
     * @return The searchResults map.
     */
    public Map<String, SearchResult> getSearchResults() {
        return searchResults;
    }

    /**
     * Gets the urlQueues map.
     * @return The urlQueues map.
     */
    public Map<String, Queue<String>> getUrlQueues() {
        return urlQueues;
    }

    /**
     * Get the foundUrlsMap.
     * @return The foundUrlsMap.
     */
    public Map<String, List<String>> getFoundUrlsMap() {
        return foundUrlsMap;
    }
}