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
		sut = new DefaultRestServiceClient.Builder(DEFAULT_URI).build();
	}

	@After
	public void tearDown() {
		if (sut.isConversationStarted()) {
			sut.closeConversation();
		}
	}
	
	@Test
	public void getResourceUri() throws URISyntaxException {
		URI result = sut.getResourceUri();
		assertEquals("Received URI does not match expected URI.", DEFAULT_URI, result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createInstanceWithIllegalNullUri() {
		@SuppressWarnings("unused")
		RestServiceClient sut = new DefaultRestServiceClient.Builder(null).build();
	}
	
	@Test
	public void getDefaultMediaType() {
		RestMediaType expectedMediaType = RestMediaType.JSON;

		RestMediaType result = sut.getMediaType();
		assertEquals("Received [mediaType] is not as expected.", expectedMediaType, result);
	}
	
	@Test
	public void setMediaType() {
		RestMediaType expectedMediaType = RestMediaType.XML;
		
		sut.setMediaType(expectedMediaType);
		
		RestMediaType result = sut.getMediaType();
		assertEquals("Received MediaType does not match set MediaType.", expectedMediaType, result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setMediaTypeToIllegalNullValue() {
		sut.setMediaType(null);
	}
	
	@Test
	public void setMediaTypeWithBuilder() {
		RestMediaType expectedMediaType = RestMediaType.XML;
		
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withMediaType(expectedMediaType)
				.build();
		
		RestMediaType result = sut.getMediaType();
		assertEquals("Received [mediaType] is not as expected.", expectedMediaType, result);
	}

	@Test
	public void getDefaultPath() {
		String expectedPath = "";
		String result = sut.getPath();
		
		assertEquals("Received [path] is not as expected.", expectedPath, result);
	}
	
	@Test
	public void setPath() {
		String expectedPath = "sample/1";
		sut.setPath(expectedPath);

		String result = sut.getPath();
		assertEquals("Received [path] is not as expected.", expectedPath, result);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setPathToIllegalNullValue() {
		sut.setPath(null);
	}

	@Test
	public void setPathWithBuilder() {
		String expectedPath = "sample/1";
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withPath(expectedPath)
				.build();
		
		String result = sut.getPath();
		assertEquals("Received [path] is not as expected.", expectedPath, result);
	}
	
	@Test
	public void getInitialResponse() {
		Response initialResponse = sut.getResponse();
		assertNull("Response is not 'null' after instance creation.", initialResponse);
	}

	@Test
	public void evaluateSuccessfulResponse() {
		Response response = Response.ok().build();
		((DefaultRestServiceClient)sut).evaluateResponse(response);
	}
	
	@Test(expected=RestServiceClientException.class)
	public void evaluateUnsuccessfulResponseForStatus5XX() {
		Response response = Response.status(Status.SERVICE_UNAVAILABLE).build();
		((DefaultRestServiceClient)sut).evaluateResponse(response);
	}

	@Test(expected=RestServiceClientException.class)
	public void evaluateUnsuccessfulResponseForStatus4XX() {
		Response response = Response.status(Status.NOT_FOUND).build();
		((DefaultRestServiceClient)sut).evaluateResponse(response);
	}

	@Test(expected=RestServiceClientException.class)
	public void evaluateUnsuccessfulResponseForStatus3XX() {
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
