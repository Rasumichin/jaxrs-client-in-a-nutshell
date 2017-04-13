package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;
import java.util.List;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Integration tests for the default implementation of type RestServiceClient.
 * This tests verify the create aspect (POST method) of the CRUD functionality.
 * 
 * @author Erik Lotz
 * @since 2016-07-10
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestServiceClientCrudIT {
	private static URI DEFAULT_URI;
	
	private RestServiceClient sut;
	
	@BeforeClass
	public static void setUpClass() throws URISyntaxException {
		DEFAULT_URI = new URI("http://jsonplaceholder.typicode.com");
	}
	
	@Before
	public void setUp() {
		sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.build();
		sut.openConversation();
	}
	
	@After
	public void tearDown() {
		if (sut.isConversationStarted()) {
			sut.closeConversation();
		}
	}

	@Test
	public void testCreateSingleCustomTypeInstance() {
		StatusType expectedStatus = Status.CREATED;
		sut.setPath("posts");
		
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

	@Test
	public void testReadExistingCustomTypeInstance() {
		int expectedId = 1;
		sut.setPath("posts/" + expectedId);
		
		JsonPlaceholderPost result = sut.read(JsonPlaceholderPost.class);
		
		assertNotNull("Conversion of JSON payload to a custom type was not correct.", result);
		assertEquals("Custom type has not the correct [id].", expectedId, result.getId());
	}

	@Test
	public void testReadNonExistingCustomTypeInstance() {
		int nonExistingId = 101;
		sut.setPath("posts/" + nonExistingId);
		
		JsonPlaceholderPost result = sut.read(JsonPlaceholderPost.class);
		
		assertNull("Found unexpected result for non-existing [id].", result);
	}

	@Test
	public void testReadListOfCustomType() {
		sut.setPath("posts");
		
		List<JsonPlaceholderPost> result = sut.readList(new GenericType<List<JsonPlaceholderPost>>() {});

		assertNotNull("Conversion of JSON payload to a list of custom types was not correct.", result);
		assertFalse("Result does not contain any element.", result.isEmpty());
		
		Object firstElementOfResult = result.get(0);
		assertTrue("Elements do not have the correct type.", firstElementOfResult instanceof JsonPlaceholderPost);
	}

	@Test
	public void testUpdateSingleCustomTypeInstance() {
		StatusType expectedStatus = Status.OK;
		JsonPlaceholderPost customType = readCustomType();
		customType.setTitle("Title has been changed.");
		sut.setPath("posts/" + customType.getId());
		
		sut.update(customType);
		
		Response response = sut.getResponse();
		assertEquals("Rest service call does not respond expected HTTP status code.", expectedStatus, response.getStatusInfo());
	}

	private JsonPlaceholderPost readCustomType() {
		sut.setPath("posts/1");
		JsonPlaceholderPost customType = sut.read(JsonPlaceholderPost.class);
		
		return customType;
	}

	@Test
	public void testDeleteSingleCustomTypeInstance() {
		StatusType expectedStatus = Status.OK;
		sut.setPath("posts");
		
		// We want to delete resource 'posts' with id '1' => 'posts/1'.
		String pathToResourceId = "1";
		sut.delete(pathToResourceId);
		
		Response response = sut.getResponse();
		assertEquals("Rest service call does not respond expected HTTP status code.", expectedStatus, response.getStatusInfo());
	}
}
