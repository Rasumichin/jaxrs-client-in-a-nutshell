package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;
import java.util.List;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Integrational tests for the default implementation of type RestServiceClient.
 * Running these tests require an existing Internet connection, because they try
 * to interact with a mocked REST endpoint provided at {@code http://jsonplaceholder.typicode.com}.
 * 
 * These tests cover the entire CRUD functionality:
 * <ul>
 *  <li>Create a resource</li>
 *  <li>Read resources</li>
 *  <li>Update a resource</li>
 *  <li>Delete a resource</li>
 * </ul>
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
		sut = DefaultRestServiceClient.newInstance(DEFAULT_URI);
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
		StatusType expected = Status.CREATED;
		sut.setPath("posts");
		
		sut.create(getJsonPlaceholderCustomType());
		
		Response response = sut.getResponse();
		StatusType actual = response.getStatusInfo();
		assertEquals("Rest service call does not respond expected HTTP status code.", expected, actual);
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
		int expected = 1;
		sut.setPath("posts/" + expected);
		
		JsonPlaceholderPost result = sut.read(JsonPlaceholderPost.class);
		
		assertNotNull("Conversion of JSON payload to a custom type was not correct.", result);
		
		int actual = result.getId();
		assertEquals("Custom type has not the correct [id].", expected, actual);
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
		boolean resultElementsHaveCorrectType = firstElementOfResult instanceof JsonPlaceholderPost;
		assertTrue("Elements do not have the correct type.", resultElementsHaveCorrectType);
	}

	@Test
	public void testUpdateSingleCustomTypeInstance() {
		StatusType expected = Status.OK;
		JsonPlaceholderPost customType = readCustomType();
		customType.setTitle("Title has been changed.");
		sut.setPath("posts/" + customType.getId());
		
		sut.update(customType);
		
		Response response = sut.getResponse();
		StatusType actual = response.getStatusInfo();
		assertEquals("Rest service call does not respond expected HTTP status code.", expected, actual);
	}

	private JsonPlaceholderPost readCustomType() {
		sut.setPath("posts/1");
		JsonPlaceholderPost customType = sut.read(JsonPlaceholderPost.class);
		
		return customType;
	}

	@Test
	public void testDeleteSingleCustomTypeInstance() {
		StatusType expected = Status.OK;

		// We want to delete resource 'posts' with id '1' => 'posts/1'.
		sut.delete("posts/1");
		
		Response response = sut.getResponse();
		StatusType actual = response.getStatusInfo();
		assertEquals("Rest service call does not respond expected HTTP status code.", expected, actual);
	}
}
