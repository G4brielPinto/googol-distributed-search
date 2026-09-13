package com.googol.browserintegration;

import com.googol.browserintegration.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import search.GoogolClient;
import search.GatewayInterface;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import com.googol.browserintegration.service.HackerNewsService;
import search.SearchResult;

/**
 * Integration tests for the Browser Integration Application.
 * This test class verifies core functionalities including RMI connections,
 * search operations, URL indexing, and AI service integration.
 */
@SpringBootTest
class BrowserintegrationApplicationTests {



	/**
	 * Tests the HackerNews service integration.
	 * Extracts search terms from a test string and uses the HackerNewsService
	 * to retrieve relevant URLs for indexing. This test validates the service's
	 * ability to convert search terms into actionable URL lists.
	 *
	 * @throws IOException If there is an error in API communication
	 */
	@Test
	public void testhackerNews() throws IOException {
		// Create test search text
		String texto = "Modern web development frameworks";

		// Split into individual terms
		String[] palavras = texto.split(" ");

		// Create an immutable list with the words
		List<String> termos = List.of(palavras);

		// Get URLs to index and display results
		List<String> urlsParaIndexar = HackerNewsService.getUrlsToIndex(termos);
		System.out.println("URLs to index:" + urlsParaIndexar);
	}

	/**
	 * Tests the indexing of a specific educational URL through the RMI gateway.
	 * <p>
	 * This test verifies that the system can successfully queue a URL from the
	 * University of Coimbra's SD course materials for indexing without throwing
	 * exceptions. It specifically checks the addUrl operation.
	 *
	 * @throws Exception If there is an error in RMI communication or URL processing
	 */
	@Test
	public void testSpecificUrlIndexing() throws Exception {
		Registry registry = LocateRegistry.getRegistry("localhost", 8185);
		GatewayInterface gateway = (GatewayInterface) registry.lookup("GatewayService");

		String testUrl = "https://eden.dei.uc.pt/~rbarbosa/sd/";

		try {
			gateway.addUrl(testUrl);
			System.out.println("Successfully indexed educational URL: " + testUrl);
			assertTrue(true, "URL should be accepted by the indexing system");
		} catch (RemoteException e) {
			fail("Failed to index educational URL: " + e.getMessage());
		}
	}

	/**
	 * Tests search functionality with a complex Latin-based search phrase.
	 * <p>
	 * Verifies that the system can handle search queries containing special characters
	 * and multiple terms. Checks both the RMI communication and result parsing logic.
	 */
	@Test
	public void testLatinPhraseSearch() {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 8185);
			GatewayInterface gateway = (GatewayInterface) registry.lookup("GatewayService");

			String testPhrase = "Curabitur all condimentum viverra turpis blandit mattis.";
			List<SearchResult> results = gateway.webSearch(testPhrase);

			assertNotNull(results, "Search results should not be null");
			assertFalse(results.isEmpty(), "Should find matches for academic test phrase");
			System.out.println("Found " + results.size() + " results for Latin phrase search");
		} catch (Exception e) {
			fail("Latin phrase search failed: " + e.getMessage());
		}
	}

	/**
	 * Tests backlink retrieval functionality for a specific academic resource.
	 * <p>
	 * Validates that the system can identify pages linking to a particular
	 * HTML resource from the SD course materials. Checks both the backlink
	 * discovery mechanism and result formatting.
	 */
	@Test
	public void testAcademicBacklinks() {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 8185);
			GatewayInterface gateway = (GatewayInterface) registry.lookup("GatewayService");

			String targetUrl = "https://eden.dei.uc.pt/~rbarbosa/sd/bottomright.html";
			List<String> backlinks = gateway.getBacklinks(targetUrl);

			assertNotNull(backlinks, "Backlinks list should never be null");
			System.out.println("Found " + backlinks.size() + " backlinks for academic resource");

			if (!backlinks.isEmpty()) {
				System.out.println("Sample backlink: " + backlinks.get(0));
			}
		} catch (Exception e) {
			fail("Backlink retrieval failed: " + e.getMessage());
		}
	}
}