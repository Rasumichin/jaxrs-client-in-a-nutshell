package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Unit test the default implementation of type RestServiceClient.
 * 
 * @author Erik Lotz
 * @since 2016-07-09
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultRestServiceClientTest {
	private static URI DEFAULT_URI;
	private RestServiceClient sut;
	
	@BeforeClass
	public static void setUpClass() throws URISyntaxException {
		DEFAULT_URI = new URI("http://jsmithy.de/resources");
	}
	
	@Before
	public void setUp() throws URISyntaxException {
		sut = DefaultRestServiceClient.newInstance(DEFAULT_URI);
	}

	@After
	public void tearDown() {
		if (sut.isConversationStarted()) {
			sut.closeConversation();
		}
	}

	@Test
	public void testNewInstance() {
		RestServiceClient sut = DefaultRestServiceClient.newInstance(DEFAULT_URI);
		
		assertNotNull("Instance could not be created!", sut);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNewInstance_with_null() {
		DefaultRestServiceClient.newInstance(null);
	}
	
	@Test
	public void testGetResourceUri() throws URISyntaxException {
		URI actual = sut.getResourceUri();
		
		assertEquals("Received URI does not match expected URI.", DEFAULT_URI, actual);
	}
	
	@Test
	public void testGetDefaultMediaType() {
		RestMediaType expected = RestMediaType.JSON;

		RestMediaType actual = sut.getMediaType();
		
		assertEquals("Received [mediaType] is not as expected.", expected, actual);
	}
	
	@Test
	public void testSetMediaType() {
		RestMediaType expected = RestMediaType.XML;
		
		sut.setMediaType(expected);
		
		RestMediaType actual = sut.getMediaType();
		assertEquals("Received MediaType does not match set MediaType.", expected, actual);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetMediaType_with_null() {
		sut.setMediaType(null);
	}
	
	@Test
	public void testGetDefaultPath() {
		String expected = "";
		
		String actual = sut.getPath();
		
		assertEquals("Received [path] is not as expected.", expected, actual);
	}
	
	@Test
	public void testSetPath() {
		String expected = "sample/1";
		
		sut.setPath(expected);

		String actual = sut.getPath();
		assertEquals("Received [path] is not as expected.", expected, actual);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetPath_with_null() {
		sut.setPath(null);
	}

	@Test
	public void testGetDefaultResponse() {
		Response result = sut.getResponse();
		
		assertNull("Response is not 'null' after instance creation.", result);
	}

	@Test
	public void testEvaluateSuccessfulResponse() {
		Response response = Response.ok().build();
		((DefaultRestServiceClient)sut).evaluateResponse(response);
	}
	
	@Test(expected=RestServiceClientException.class)
	public void testEvaluateUnsuccessfulResponseForStatus5XX() {
		Response response = Response.status(Status.SERVICE_UNAVAILABLE).build();
		((DefaultRestServiceClient)sut).evaluateResponse(response);
	}

	@Test(expected=RestServiceClientException.class)
	public void testEvaluateUnsuccessfulResponseForStatus4XX() {
		Response response = Response.status(Status.NOT_FOUND).build();
		((DefaultRestServiceClient)sut).evaluateResponse(response);
	}

	@Test(expected=RestServiceClientException.class)
	public void testEvaluateUnsuccessfulResponseForStatus3XX() {
		Response response = Response.status(Status.USE_PROXY).build();
		((DefaultRestServiceClient)sut).evaluateResponse(response);
	}
	
	@Test
	public void testToString() {
		String expected = "DefaultRestServiceClient [resourceUri=http://jsmithy.de/resources]";
		
		String actual = sut.toString();
		
		assertEquals("String representation is not correct!", expected, actual);
	}
}
