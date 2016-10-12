package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import java.net.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.junit.*;

/**
 * Unit test the default implementation of type RestServiceClient.
 * 
 * @author Erik Lotz
 * @since 2016-07-09
 * 
 */
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
		MediaType expectedMediaType = MediaType.valueOf(MediaType.APPLICATION_JSON);

		MediaType result = sut.getMediaType();
		assertEquals("Received [mediaType] is not as expected.", expectedMediaType, result);
	}
	
	@Test
	public void setMediaType() {
		MediaType expectedMediaType = MediaType.valueOf(MediaType.APPLICATION_XML);
		
		sut.setMediaType(expectedMediaType);
		MediaType result = sut.getMediaType();
		assertEquals("Received MediaType does not match set MediaType.", expectedMediaType, result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setMediaTypeToIllegalNullValue() {
		sut.setMediaType(null);
	}
	
	@Test
	public void setMediaTypeWithBuilder() {
		MediaType expectedMediaType = MediaType.valueOf(MediaType.APPLICATION_XML);
		RestServiceClient sut = new DefaultRestServiceClient.Builder(DEFAULT_URI)
				.withMediaType(expectedMediaType)
				.build();
		
		MediaType result = sut.getMediaType();
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
	public void asserThatRestClientIsAvailableAfterInstanceHasBeenCreated() {
		Client restClient = ((DefaultRestServiceClient)sut).getRestClient();
		
		assertNotNull("[restClient] has not been initialized correctly.", restClient);
	}
	
	@Test
	public void asserThatWebTargetIsAvailableAfterInstanceHasBeenCreated() {
		WebTarget webTarget = ((DefaultRestServiceClient)sut).getWebTarget();
		
		assertNotNull("[webTarget] has not been initialized correctly.", webTarget);
		assertEquals("URI of [webTarget] has not been set correctly.", DEFAULT_URI, webTarget.getUri());
	}
	
	@Test(expected=IllegalStateException.class)
	public void finalizeLeadsToRestClientNoLongerUsable() throws Throwable {
		Client restClient = ((DefaultRestServiceClient)sut).getRestClient();
		
		// Invoke an arbitrary method which is perfectly legal.
		restClient.target("");
		
		// Finalize our sut which closes the 'restClient'.
		((DefaultRestServiceClient)sut).finalize();
		
		// Invoke again a method which is now no longer legal.
		restClient.target("");
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
}
