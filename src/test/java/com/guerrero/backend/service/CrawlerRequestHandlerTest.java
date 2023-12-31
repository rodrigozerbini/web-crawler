package com.guerrero.backend.service;

import com.guerrero.backend.dto.CrawlResponse;
import com.guerrero.backend.model.SearchResult;
import com.guerrero.backend.model.Status;
import com.google.gson.*;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CrawlerRequestHandlerTest {

    @Test
    void testGetResults() {
        // Arrange
        CrawlerService crawlerService = mock(CrawlerService.class);
        CrawlerRequestHandler requestHandler = new CrawlerRequestHandler(crawlerService);
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        String searchId = "abc123d4";
        when(request.params("id")).thenReturn(searchId);

        SearchResult searchResult = new SearchResult(searchId);
        String url1 = "http://test.com";
        String url2 = "http://example.com";
        searchResult.addUrl(url1);
        searchResult.addUrl(url2);
        searchResult.setStatus(Status.done);
        when(crawlerService.getSearchResult(searchId)).thenReturn(searchResult);

        // Act
        String jsonResponse = requestHandler.getResults(request, response);

        // Assert
        assertNotNull(jsonResponse);
        verify(response).type("application/json");
        verify(response).status(200);

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        assertEquals(searchId, jsonObject.get("id").getAsString());
        assertEquals("done", jsonObject.get("status").getAsString());

        JsonArray urlsArray = jsonObject.getAsJsonArray("urls");
        assertEquals(2, urlsArray.size());
        assertTrue(urlsArray.contains(new JsonPrimitive(url1)));
        assertTrue(urlsArray.contains(new JsonPrimitive(url2)));
    }

    @Test
    void testStartSearch() {
        // Arrange
        CrawlerService crawlerService = mock(CrawlerService.class);
        CrawlerRequestHandler requestHandler = new CrawlerRequestHandler(crawlerService);

        // Mock the Spark Request and Response
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        // Mock the JSON body in the request
        when(request.body()).thenReturn("{ \"keyword\": \"testing\" }");

        // Mock the searchId generated by CrawlerService
        String mockSearchId = "mockSearchId";
        when(crawlerService.initializeDataStructures(anyString())).thenReturn(mockSearchId);

        // Mock the asynchronous crawl operation
        doNothing().when(crawlerService).crawl(mockSearchId);

        // Act
        String jsonResponse = requestHandler.startSearch(request, response);

        // Assert
        verify(response).type("application/json");
        verify(response).status(200);
        verify(crawlerService).initializeDataStructures("testing");

        // Wait for the asynchronous operation to complete
        CompletableFuture<Void> asyncOperation = CompletableFuture.runAsync(() -> {
            verify(crawlerService).crawl(mockSearchId);
        });
        // Join the CompletableFuture to wait for completion
        asyncOperation.join();

        // Verify that the returned JSON response contains the expected searchId
        CrawlResponse crawlResponse = new Gson().fromJson(jsonResponse, CrawlResponse.class);
        assertEquals(mockSearchId, crawlResponse.getId());
    }
}
