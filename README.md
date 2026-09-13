# Googol Distributed Search Engine

An academic distributed-search project with two Maven modules:

- Project: Java/RMI crawler, URL queue, gateway and indexing-barrel components.
- browserintegration: Spring Boot web interface that communicates with the search system and offers
  an optional Gemini-powered explanation feature.

## Security boundary

This is a fresh, sanitised repository. It contains no API keys, local databases, crawler indexes,
compiled artefacts, reports or historical Git data.

The optional Gemini integration reads GEMINI_API_KEY from the environment. If the variable is
absent, the web interface returns a clear unavailable-feature message instead of attempting a
remote request.

Never commit a .env file or a real API key.

## Repository layout

~~~text
.
├── Project/
│   └── src/main/java/search/
├── browserintegration/
│   ├── src/main/java/
│   ├── src/main/resources/
│   └── src/test/java/
├── .env.example
└── pom.xml
~~~

## Prerequisites

- JDK 21
- Maven 3.9 or newer
- a local Java RMI setup for the distributed components

## Build

From the repository root:

~~~bash
mvn clean package
~~~

The system was developed as a multi-process academic exercise. Start the RMI services from the
Project module before using the Spring web interface. The default sample configuration uses
localhost and can be adapted for a local multi-process setup.

## Optional Gemini configuration

Set an environment variable only in the shell or deployment environment that runs the web module.

~~~bash
set GEMINI_API_KEY=your_key_here
~~~

On PowerShell:

~~~powershell
$env:GEMINI_API_KEY = "your_key_here"
~~~

Do not place the actual value in source code, test files or properties files.

## Limitations

- The original local search index and SQLite data are intentionally excluded.
- Integration tests that required a live Gemini key were removed.
- A full distributed run requires separate RMI services and is not performed as part of this
  sanitisation.
- The repository is initially private while it is reviewed for public portfolio release.

## Privacy and sharing

No credentials, search databases, crawled content, reports, personal contact details or local
machine paths are included.
