package de.jsmithy.rest.jaxrs.nutshell.client;

import java.net.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

/**
 * This class delivers the default implementation of type RestServiceClient.
 * 
 * @author Erik Lotz
 * @since 2016-07-09
 * 
 */
public class DefaultRestServiceClient implements RestServiceClient {
	private URI resourceUri;
	private String path;
	private RestMediaType mediaType;
	private Client restClient;
	private Response response;

	/**
	 * Add a default constructor with this visibility just for
	 * ONE purpose: Allow stubbing of instances of this type while
	 * doing unit tests.
	 * 
	 * Creates an instance of this type with a FICTIONAL URI.
	 * 
	 * @throws URISyntaxException
	 * 
	 */
	DefaultRestServiceClient() throws URISyntaxException {
		this(new URI("http://jsmithy.de/resources"));
	}
	
	private DefaultRestServiceClient(URI anUri) {
		setResourceUri(anUri);
	}

	private void setResourceUri(URI anUri) {
		if (anUri == null) {
			throw new IllegalArgumentException("Argument [anUri] must not be 'null'.");
		}
		resourceUri = anUri;
	}
	
	public static class Builder {
		private final URI resourceUri;
		private String path = "";
		private RestMediaType mediaType = RestMediaType.JSON;

		public Builder(URI anUri) {
			resourceUri = anUri;
		}

		public Builder withPath(String aPath) {
			path = aPath;
			return this;
		}
		
		public Builder withMediaType(RestMediaType aMediaType) {
			mediaType = aMediaType;
			return this;
		}

		public RestServiceClient build() {
			DefaultRestServiceClient restServiceClient = new DefaultRestServiceClient(resourceUri);
			restServiceClient.setPath(path);
			restServiceClient.setMediaType(mediaType);
			
			return restServiceClient;
		}
	}
	
	@Override
	public void openConversation() {
		if (isConversationStarted()) {
			throw new IllegalStateException("Cannot open a conversation when it has been already openend.");
		}
		initRestClient();
	}

	@Override
	public boolean isConversationStarted() {
		return getRestClient() != null;
	}

	private void initRestClient() {
		restClient = createRestClient();
	}

	// This 'private' method is package protected for testing purposes.
	Client createRestClient() {
		return ClientBuilder.newClient();
	}
	
	// This 'private' method is package protected for testing purposes.
	Client getRestClient() {
		return restClient;
	}
	
	@Override
	public void closeConversation() {
		if (!isConversationStarted()) {
			throw new IllegalStateException("Cannot close conversation before it has been started.");
		}
		destroyRestClient();
	}

	private void destroyRestClient() {
		getRestClient().close();
		restClient = null;
	}

	@Override
	public URI getResourceUri() {
		return resourceUri;
	}

	@Override
	public Response getResponse() {
		return response;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String aPath) {
		if (aPath == null) {
			throw new IllegalArgumentException("Argument [aPath] must not be 'null'.");
		}
		path = aPath;
	}

	@Override
	public RestMediaType getMediaType() {
		return mediaType ;
	}

	@Override
	public void setMediaType(RestMediaType aMediaType) {
		if (aMediaType == null) {
			throw new IllegalArgumentException("Argument [aMediaType] must not be 'null'.");
		}
		mediaType = aMediaType;
	}
	
	private MediaType getJaxRsMediaType() {
		return getMediaType().getMediaType();
	}

	@Override
	public <T> void create(T type) {
		Entity<T> entity = getMediaType().getEntity(type);
		response = getRestClient()
				.target(getResourceUri())
				.path(getPath())
				.request(getJaxRsMediaType())
				.post(entity, Response.class);
	}

	@Override
	public <T> T read(Class<T> type) {
		T result = null;
		response = getRestClient()
				.target(getResourceUri())
				.path(getPath())
				.request(getJaxRsMediaType())
				.get(Response.class);
		
		// TODO (EL, 2016-07-16): replace this with response evaluation type that might throw a runtime exception.
		if (getResponse().getStatusInfo() == Status.OK) {
			if (getResponse().hasEntity()) {
				result = getResponse().readEntity(type);
			}
		}
		
		return result;
	}

	@Override
	public <T> T readList(GenericType<T> genericType) {
		T result = null;
		response = getRestClient()
				.target(getResourceUri())
				.path(getPath())
				.request(getJaxRsMediaType())
				.get(Response.class);

		// TODO (EL, 2016-07-16): replace this with response evaluation type that might throw a runtime exception.
		if (getResponse().getStatusInfo() == Status.OK) {
			if (getResponse().hasEntity()) {
				result = getResponse().readEntity(genericType);
			}
		}
		
		return result;
	}

	@Override
	public <T> void update(T type) {
		Entity<T> entity = getMediaType().getEntity(type);
		response = getRestClient()
				.target(getResourceUri())
				.path(getPath())
				.request(getJaxRsMediaType())
				.put(entity, Response.class);
	}

	@Override
	public void delete(String pathToResourceId) {
		String pathWithResourceId = getPath() + '/' + pathToResourceId;
		response = getRestClient()
				.target(getResourceUri())
				.path(pathWithResourceId)
				.request()
				.delete();
	}

	void evaluateResponse(Response aResponse) {
		Family statusFamily = Response.Status.Family.familyOf(aResponse.getStatus());
		// TODO (EL, 2016-07-24): Come back to this and check whether further differentiation might be required.
		if (!statusFamily.equals(Response.Status.Family.SUCCESSFUL)) {
			throw new RestServiceClientException(this);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()
				+ " [resourceUri=" + getResourceUri()
				+ "]";
	}
}
