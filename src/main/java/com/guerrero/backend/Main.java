package com.guerrero.backend;

import com.guerrero.backend.service.CrawlerService;
import com.guerrero.backend.service.CrawlerRequestHandler;

import static spark.Spark.*;

/**
 * The main class responsible for starting the crawler application.
 * Initializes the CrawlerService and handles HTTP requests using the CrawlerRequestHandler.
 */
public class Main {
    public static void main(String[] args) {

        String baseUrl = System.getenv("BASE_URL");
        CrawlerService crawlerService = new CrawlerService(baseUrl);
        CrawlerRequestHandler crawlerRequestHandler = new CrawlerRequestHandler(crawlerService);

        get(CrawlerRequestHandler.GET_RESULTS_PATH, crawlerRequestHandler::getResults);
        post(CrawlerRequestHandler.START_SEARCH_PATH, crawlerRequestHandler::startSearch);
    }
}
