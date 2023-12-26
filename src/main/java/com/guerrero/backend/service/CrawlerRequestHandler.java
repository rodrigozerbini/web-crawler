package com.guerrero.backend.service;

import com.guerrero.backend.dto.CrawlResponse;
import com.guerrero.backend.dto.ResultsResponse;
import com.guerrero.backend.dto.SearchRequest;
import com.guerrero.backend.model.SearchResult;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.concurrent.CompletableFuture;

/**
 * Handles HTTP requests related to the crawler functionality.
 * This class defines methods to start a new search
 * and retrieve results based on a search ID.
 */
public class CrawlerRequestHandler {

    /**
     * HTTP path for retrieving search results.
     */
    public final static String GET_RESULTS_PATH = "/crawl/:id";

    /**
     * HTTP path for initiating a new search.
     */
    public final static String START_SEARCH_PATH = "/crawl";

    /**
     * The associated CrawlerService instance that performs
     * search initiation and result retrieval.
     */
    private final CrawlerService crawlerService;

    /**
     * Constructs a CrawlerRequestHandler with the given CrawlerService.
     * @param crawlerService The CrawlerService instance to handle requests.
     */
    public CrawlerRequestHandler(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    /**
     * Handles HTTP GET requests to retrieve search results.
     * @param request  The Spark request object.
     * @param response The Spark response object.
     * @return A JSON representation of the search results.
     */
    public String getResults(Request request, Response response) {
        String searchId = request.params("id");
        ResultsResponse resultsResponse = createResultsResponse(searchId);
        String jsonResponse = new Gson().toJson(resultsResponse);
        response.type("application/json");
        response.status(200);
        return jsonResponse;
    }

    /**
     * Creates a ResultsResponse based on the provided search ID.
     * @param searchId The ID of the search for which results are requested.
     * @return A ResultsResponse containing search results information.
     */
    private ResultsResponse createResultsResponse(String searchId) {
        SearchResult searchResult = crawlerService.getSearchResult(searchId);
        return new ResultsResponse(
                searchResult.getId(),
                searchResult.getStatus(),
                searchResult.getUrls()
        );
    }

    /**
     * Handles HTTP POST requests to initiate a new search.
     * @param request  The Spark request object.
     * @param response The Spark response object.
     * @return A JSON representation containing the id of the initiated search.
     */
    public String startSearch(Request request, Response response) {
        String keyword = getKeyword(request);
        String searchId = initiateCrawlerService(keyword);
        CrawlResponse crawlResponse = new CrawlResponse(searchId);
        String jsonResponse = new Gson().toJson(crawlResponse);
        response.type("application/json");
        response.status(200);
        return jsonResponse;
    }

    /**
     * Initiates a new search using the CrawlerService asynchronously.
     * @param keyword The keyword for the new search.
     * @return The ID of the initiated search.
     */
    private String initiateCrawlerService(String keyword) {
        String searchId = crawlerService.initializeDataStructures(keyword);
        CompletableFuture.runAsync(() -> crawlerService.crawl(searchId));
        return searchId;
    }

    /**
     * Extracts the keyword from the Spark request body.
     * @param request The Spark request object.
     * @return The keyword extracted from the request.
     */
    private String getKeyword(Request request) {
        SearchRequest searchRequest = new Gson().fromJson(request.body(), SearchRequest.class);
        return searchRequest.getKeyword();
    }
}
