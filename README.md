Objective: Develop a Java application to navigate a website in search of a user-provided term and list the URLs where the term is found.

Requirements:

1. User interaction with the application should occur through an HTTP API, made available on port 4567. Two operations must be supported:

a. POST: Initiates a new search for a term (keyword).

Request:

POST /crawl HTTP/1.1

Host: localhost:4567

Content-Type: application/json

Body: {"keyword": "domain"}

Response:
200 OK
Content-Type: application/json
Body: {"id": "30vbllyb"}

b. GET: Queries search results.
Request:
GET /crawl/30vbllyb HTTP/1.1
Host: localhost:4567

200 OK
Content-Type: application/json
{
  "id": "30vbllyb",
  "status": "active",
  "urls": [
    "https://example.com/",
  ]
}

2. The searched term must have a minimum of 4 and a maximum of 32 characters. The search should be case-insensitive, considering any part of the HTML content (including tags and comments).

3. The search's ID must be an automatically generated 8-character alphanumeric code.

4. The base URL of the website for the analyses is determined by an environment variable. Searches should follow links (absolute and relative) in anchor elements of visited pages only if they have the same base URL.

5. The application must support the execution of multiple simultaneous searches. Information about ongoing (status active) or completed (status done) searches must be kept indefinitely while the application is running.

6. While a search is in progress, its partially found results should be returned by the GET operation.

7. The project must follow the provided base structure. The Dockerfile and pom.xml files cannot be modified. Any other provided file can be modified.

8. From the project's root directory, the following two commands, executed in sequence, should compile and start the application:

docker build . -t guerrero-webcrawler
docker run -e BASE_URL=https://www.example.com/ -p 4567:4567 --rm guerrero-webcrawler
