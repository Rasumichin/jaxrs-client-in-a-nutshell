package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;
import java.util.List;

import javax.ws.rs.core.GenericType;

import org.junit.*;

/**
 * Integration tests for the default implementation of type RestServiceClient.
 * This tests verify the read aspect (GET method) of the CRUD functionality.
 * 
 * @author Erik Lotz
 * @since 2016-07-10
 * 
 */
public class RestServiceClientReadIT {
	private static URI DEFAULT_URI;
	
	@BeforeClass
	public static void setUpClass() throws URISyntaxException {
		DEFAULT_URI = new URI("http://jsonplaceholder.typicode.com");
	}

	@Test
	public void readExistingCustomTypeInstance() {
		int expectedId = 1;
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withPath("posts/" + expectedId)
				.build();
		
		JsonPlaceholderPost result = sut.read(JsonPlaceholderPost.class);
		
		assertNotNull("Conversion of JSON payload to a custom type was not correct.", result);
		assertEquals("Custom type has not the correct [id].", expectedId, result.getId());
	}

	@Test
	public void readNonExistingCustomTypeInstance() {
		int nonExistingId = 101;
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withPath("posts/" + nonExistingId)
				.build();
		
		JsonPlaceholderPost result = sut.read(JsonPlaceholderPost.class);
		
		assertNull("Found unexpected result for non-existing [id].", result);
	}

	@Test
	public void readListOfCustomType() {
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withPath("posts")
				.build();
		
		List<JsonPlaceholderPost> result = sut.read(new GenericType<List<JsonPlaceholderPost>>() {});

		assertNotNull("Conversion of JSON payload to a list of custom types was not correct.", result);
		assertFalse("Result does not contain any element.", result.isEmpty());
		
		Object firstElementOfResult = result.get(0);
		assertTrue("Elements do not have the correct type.", firstElementOfResult instanceof JsonPlaceholderPost);
	}
}
