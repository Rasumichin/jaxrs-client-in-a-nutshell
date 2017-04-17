package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;
import java.util.List;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Using this integration test to play with the JAX-RS client API.
 * 
 * @author Erik Lotz
 * @since 2016-07-02
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NativeRestServiceClientIT {
	private static final String JSON_TEST_URI = "http://jsonplaceholder.typicode.com";
	private static URI JSON_PLACEHOLDER_URI;
	
	private Client restClient;

	@BeforeClass
	public static void setUpClass() throws URISyntaxException {
		JSON_PLACEHOLDER_URI = new URI(JSON_TEST_URI);
	}
	
	@Before
	public void setUp() {
		restClient = ClientBuilder.newClient();
	}

	@After
	public void tearDown() {
		restClient.close();
	}

	@Test
	public void testGetRequest_readCustomTypeFromResponse() {
		StatusType expectedStatus = Status.OK;

		Response result = restClient.target(JSON_PLACEHOLDER_URI)
				.path("posts/1")
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		assertEquals("Expected HTTP status is not matched!", expectedStatus, result.getStatusInfo());
		assertTrue("Response does not contain an entity in its body!", result.hasEntity());
		
		JsonPlaceholderPost resultType = result.readEntity(JsonPlaceholderPost.class);
		assertNotNull("Conversion of JSON payload to custom type was not correct!", resultType);
		assertEquals("Custom type has not the correct [id]!", 1, resultType.getId());
	}

	@Test
	public void testGetRequest_readListOfCustomTypeFromResponse() {
		StatusType expectedStatus = Status.OK;

		// According to 'http://jsonplaceholder.typicode.com/posts' this should always return 100 elements.
		int expectedSize = 100;

		Response result = restClient.target(JSON_PLACEHOLDER_URI)
				.path("posts")
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		assertEquals("Expected HTTP status is not matched!", expectedStatus, result.getStatusInfo());
		
		List<JsonPlaceholderPost> resultTypeList = result.readEntity(new GenericType<List<JsonPlaceholderPost>>() {});
		assertEquals("Response body does not contain the correct number of elements!", expectedSize, resultTypeList.size());
	}

	@Test
	public void testPostRequest_verifyResponseType() {
		StatusType expectedStatus = Status.CREATED;
		JsonPlaceholderPost customType = getJsonPlaceholderCustomType();
		
		Response result = restClient.target(JSON_PLACEHOLDER_URI)
			.path("posts")
			.request(MediaType.APPLICATION_JSON)
			.post(Entity.json(customType), Response.class);
		
		assertEquals("Expected HTTP status is not matched!", expectedStatus, result.getStatusInfo());
		assertTrue("Response does not contain any content in its body!", result.hasEntity());
		
		JsonPlaceholderPost resultType = result.readEntity(JsonPlaceholderPost.class);
		assertEquals("Response body does not correspond to provided custom type!", customType, resultType);
		assertEquals("Response contains unexpected Hypermedia links!", 0, result.getLinks().size());
	}
	
	@Test
	public void testDeleteRequest() {
		StatusType expectedStatus = Status.OK;
		
		Response result = restClient.target(JSON_PLACEHOLDER_URI)
				.path("posts/1")
				.request()
				.delete();
		
		assertEquals("Expected HTTP status is not matched!", expectedStatus, result.getStatusInfo());
	}

	@Test
	public void testPutRequest_comparePassedCustomTypeWithReturnedCustomType() {
		StatusType expectedStatus = Status.OK;
		JsonPlaceholderPost customType = getJsonPlaceholderCustomType();
		customType.setId(1);
		
		Response result = restClient.target(JSON_PLACEHOLDER_URI)
			.path("posts/1")
			.request(MediaType.APPLICATION_JSON)
			.put(Entity.json(customType), Response.class);
		
		assertEquals("Expected HTTP status is not matched!", expectedStatus, result.getStatusInfo());
		
		JsonPlaceholderPost resultType = result.readEntity(JsonPlaceholderPost.class);
		assertEquals("Response body does not correspond to provided custom type!", customType, resultType);
	}

	@Test
	public void testGetRequest_thenPutRequest() {
		StatusType expectedStatus = Status.OK;

		Response result = restClient.target(JSON_PLACEHOLDER_URI)
				.path("posts/1")
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		assertEquals("Expected HTTP status is not matched!", expectedStatus, result.getStatusInfo());
		
		JsonPlaceholderPost postWithId1 = result.readEntity(JsonPlaceholderPost.class);
		postWithId1.setTitle("Some changed title");

		result = restClient.target(JSON_PLACEHOLDER_URI)
				.path("posts/1")
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.json(postWithId1), Response.class);

		assertEquals("Expected HTTP status is not matched!", expectedStatus, result.getStatusInfo());
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
