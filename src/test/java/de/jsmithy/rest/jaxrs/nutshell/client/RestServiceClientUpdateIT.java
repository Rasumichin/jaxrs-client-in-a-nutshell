package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.*;

import org.junit.*;

/**
 * Integration tests for the default implementation of type RestServiceClient.
 * This tests verify the update aspect (PUT method) of the CRUD functionality.
 * 
 * @author Erik Lotz
 * @since 2016-07-10
 * 
 */
public class RestServiceClientUpdateIT {
	private static URI DEFAULT_URI;
	
	@BeforeClass
	public static void setUpClass() throws URISyntaxException {
		DEFAULT_URI = new URI("http://jsonplaceholder.typicode.com");
	}

	@Test
	public void updateSingleCustomTypeInstance() {
		StatusType expectedStatus = Status.OK;
		JsonPlaceholderPost customType = readCustomType();
		customType.setTitle("Title has been changed.");
		
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withPath("posts/" + customType.getId())
				.build();
		
		sut.update(customType);
		
		Response response = sut.getResponse();
		assertEquals("Rest service call does not respond expected HTTP status code.", expectedStatus, response.getStatusInfo());
	}

	private JsonPlaceholderPost readCustomType() {
		RestServiceClient readService = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withPath("posts/1")
				.build();
		JsonPlaceholderPost customType = readService.read(JsonPlaceholderPost.class);
		
		return customType;
	}
}
