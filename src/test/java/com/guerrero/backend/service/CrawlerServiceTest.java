package com.guerrero.backend.service;

import com.guerrero.backend.model.Search;
import com.guerrero.backend.model.SearchResult;
import com.guerrero.backend.model.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CrawlerServiceTest {

    private static String baseUrl;
    private static String validKeyword;

    @BeforeAll
    static void setup() {
        baseUrl = "http://hiring.axreng.com/";
        validKeyword = "Christmas";
    }

    @Test
    void testInitializeDataStructures_InvalidKeyword() {
        // Arrange
        String invalidKeyword = "toy";
        CrawlerService crawlerService = new CrawlerService(baseUrl);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> crawlerService.initializeDataStructures(invalidKeyword));
    }

    @Test
    void testInitializeDataStructures_ValidKeyword() {
        // Arrange
        CrawlerService crawlerServiceSpy = spy(new CrawlerService(baseUrl));
        Search searchMock = mock(Search.class);
        String mockId = "abc123d4";
        int expectedCollectionSize = 1;
        when(crawlerServiceSpy.createSearch(validKeyword)).thenReturn(searchMock);
        when(searchMock.getId()).thenReturn(mockId);

        // Act
        String searchId = crawlerServiceSpy.initializeDataStructures(validKeyword);

        // Assert
        assertEquals(mockId, searchId);
        assertEquals(expectedCollectionSize, crawlerServiceSpy.getSearches().size());
        assertEquals(expectedCollectionSize, crawlerServiceSpy.getSearchResults().size());
        assertEquals(expectedCollectionSize, crawlerServiceSpy.getUrlQueues().size());
        assertEquals(expectedCollectionSize, crawlerServiceSpy.getFoundUrlsMap().size());
        assertEquals(mockId, crawlerServiceSpy.getSearchResults().get(mockId).getId());
        assertTrue(crawlerServiceSpy.getUrlQueues().get(mockId).contains(baseUrl));
    }

    @Test
    void testGetSearchResult() {
        // Arrange
        CrawlerService crawlerService = new CrawlerService(baseUrl);
        String searchId = crawlerService.initializeDataStructures(validKeyword);
        crawlerService.getSearchResults().get(searchId).getUrls().add(baseUrl);
        int expectedUrlsSetSize = 1;

        // Act
        SearchResult searchResult = crawlerService.getSearchResult(searchId);

        // Assert
        assertNotNull(searchResult);
        assertEquals(searchId, searchResult.getId());
        assertEquals(Status.active, searchResult.getStatus());
        assertEquals(expectedUrlsSetSize, searchResult.getUrls().size());
        assertTrue(searchResult.getUrls().contains(baseUrl));
    }

    @Test
    void testHtmlContainsKeyword_TrueCase() {
        // Arrange
        String rawHTML = "Happy Christmas, my friend";
        CrawlerService crawlerService = new CrawlerService(baseUrl);

        // Act
        boolean htmlContainsKeyword = crawlerService.htmlContainsKeyword(validKeyword, rawHTML);

        // Assert
        assertTrue(htmlContainsKeyword);
    }

    @Test
    void testHtmlContainsKeyword_FalseCase() {
        // Arrange
        String rawHTML = "My raw HTML";
        CrawlerService crawlerService = new CrawlerService(baseUrl);

        // Act
        boolean htmlContainsKeyword = crawlerService.htmlContainsKeyword(validKeyword, rawHTML);

        // Assert
        assertFalse(htmlContainsKeyword);
    }

    @Test
    void testCrawl_KeywordNotFound() {
        // Arrange
        String basePath = System.getProperty("user.dir");
        String filePath = "/testPage.html";
        String url = "file:" + basePath + filePath;
        CrawlerService crawlerService = new CrawlerService(url);
        String searchId = crawlerService.initializeDataStructures(validKeyword);

        // Act
        crawlerService.crawl(searchId);

        // Assert
        assertTrue(crawlerService.getUrlQueues().get(searchId).isEmpty());
        assertTrue(crawlerService.getSearchResult(searchId).getUrls().isEmpty());
        assertEquals(Status.done, crawlerService.getSearchResult(searchId).getStatus());
    }

    @Test
    void testCrawl_KeywordFound() {
        // Arrange
        String basePath = System.getProperty("user.dir");
        //String filePath = "/src/test/resources/testPage.html";
        String filePath = "/testPage.html";
        String url = "file:" + basePath + filePath;
        String keyword = "text";
        int expectedUrlsSetSize = 1;
        CrawlerService crawlerService = new CrawlerService(url);
        String searchId = crawlerService.initializeDataStructures(keyword);

        // Act
        crawlerService.crawl(searchId);

        // Assert
        assertTrue(crawlerService.getUrlQueues().get(searchId).isEmpty());
        assertEquals(expectedUrlsSetSize, crawlerService.getSearchResult(searchId).getUrls().size());
        assertTrue(crawlerService.getSearchResult(searchId).getUrls().contains(url));
        assertEquals(Status.done, crawlerService.getSearchResult(searchId).getStatus());
    }
}
