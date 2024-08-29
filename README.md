# Web Crawler API

## Objective

Develop a Java application that navigates a website to search for a user-provided term and lists the URLs where the term is found.

## Requirements

### User Interaction

User interaction with the application occurs through an HTTP API, available on port `4567`. The API supports two operations:

#### 1. Initiate a New Search

**Method:** `POST`  
**Endpoint:** `/crawl`  

**Request Example:**
```
POST /crawl HTTP/1.1
Host: localhost:4567
Content-Type: application/json

Body: {"keyword": "domain"}
```
**Response Example:**
```
HTTP/1.1 200 OK
Content-Type: application/json

Body: {"id": "30vbllyb"}
```
This operation initiates a new search for the specified keyword. The response returns a unique id for the search.

#### 2. Query Search Results

**Method:** `GET`  
**Endpoint:** `/crawl/{id}` 

**Request Example:**
```
GET /crawl/30vbllyb HTTP/1.1
Host: localhost:4567
```
**Response Example:**
```
HTTP/1.1 200 OK
Content-Type: application/json

Body: {"id": "30vbllyb", "status": "active", "urls": ["https://example.com/"]}
```
This operation queries the search results for the specified id.

### Additional Requirements

- The searched term must have a minimum of 4 and a maximum of 32 characters.
- The search should be case-insensitive, considering any part of the HTML content (including tags and comments).
- The search's ID must be an automatically generated 8-character alphanumeric code.
- The base URL of the website for the analysis is determined by an environment variable.
- Searches should follow links (absolute and relative) in anchor elements of visited pages only if they have the same base URL.
- The application must support the execution of multiple simultaneous searches.
- Information about ongoing (status active) or completed (status done) searches must be kept indefinitely while the application is running.
- While a search is in progress, its partially found results should be returned by the GET operation.

### Building and Running the Application

From the project's root directory, the following two commands, executed in sequence, should compile and start the application:
```
docker build . -t guerrero-webcrawler
```
```
docker run -e BASE_URL=https://www.example.com/ -p 4567:4567 --rm guerrero-webcrawler
```
Change the BASE_URL variable to use the web crawler on another website.
