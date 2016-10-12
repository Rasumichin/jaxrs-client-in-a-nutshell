package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.*;

import org.junit.*;

/**
 * Integration tests for the default implementation of type RestServiceClient.
 * This tests verify the create aspect (POST method) of the CRUD functionality.
 * 
 * @author Erik Lotz
 * @since 2016-07-10
 * 
 */
public class RestServiceClientCreateIT {
	private static URI DEFAULT_URI;
	
	@BeforeClass
	public static void setUpClass() throws URISyntaxException {
		DEFAULT_URI = new URI("http://jsonplaceholder.typicode.com");
	}

	@Test
	public void createSingleCustomTypeInstance() {
		StatusType expectedStatus = Status.CREATED;
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withPath("posts")
				.build();
		
		sut.create(getJsonPlaceholderCustomType());
		
		Response response = sut.getResponse();
		assertEquals("Rest service call does not respond expected HTTP status code.", expectedStatus, response.getStatusInfo());
	}

	private JsonPlaceholderPost getJsonPlaceholderCustomType() {
		JsonPlaceholderPost customType = new JsonPlaceholderPost();
		customType.setId(101);
		customType.setUserId(10);
		customType.setTitle("my custom title");
		customType.setBody("my custom body");
		
		return customType;
	}
}
