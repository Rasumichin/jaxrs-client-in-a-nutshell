package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.*;

import org.junit.*;

/**
 * Integration tests for the default implementation of type RestServiceClient.
 * This tests verify the delete aspect (DELETE method) of the CRUD functionality.
 * 
 * @author Erik Lotz
 * @since 2016-07-10
 * 
 */
public class RestServiceClientDeleteIT {
	private static URI DEFAULT_URI;
	
	@BeforeClass
	public static void setUpClass() throws URISyntaxException {
		DEFAULT_URI = new URI("http://jsonplaceholder.typicode.com");
	}

	@Test
	public void deleteSingleCustomTypeInstance() {
		StatusType expectedStatus = Status.OK;
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withPath("posts")
				.build();
		
		// We want to delete resource 'posts' with id '1' => 'posts/1'.
		String pathToResourceId = "1";
		sut.delete(pathToResourceId);
		
		Response response = sut.getResponse();
		assertEquals("Rest service call does not respond expected HTTP status code.", expectedStatus, response.getStatusInfo());
	}
}
