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
	private MediaType mediaType;
	private WebTarget webTarget;
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
		private MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_JSON);

		public Builder(URI anUri) {
			resourceUri = anUri;
		}

		public Builder withPath(String aPath) {
			path = aPath;
			return this;
		}
		
		public Builder withMediaType(MediaType aMediaType) {
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
		webTarget = getRestClient().target(getResourceUri());
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
		webTarget = null;
		restClient = null;
	}

	// This 'private' method is package protected for testing purposes.
	WebTarget getWebTarget() {
		return webTarget;
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
	public MediaType getMediaType() {
		return mediaType ;
	}

	@Override
	public void setMediaType(MediaType aMediaType) {
		if (aMediaType == null) {
			throw new IllegalArgumentException("Argument [aMediaType] must not be 'null'.");
		}
		mediaType = aMediaType;
	}

	@Override
	public <T> void create(T type) {
		response = getWebTarget()
				.path(getPath())
				.request(getMediaType())
				// TODO (EL, 2016-07-10): fix JSON format here; check how to implement this with regard to MediaType.
				.post(Entity.json(type), Response.class);
	}

	@Override
	public <T> T read(Class<T> type) {
		T result = null;
		response = getWebTarget()
				.path(getPath())
				.request(getMediaType())
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
		response = getWebTarget()
				.path(getPath())
				.request(getMediaType())
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
		response = getWebTarget()
				.path(getPath())
				.request(getMediaType())
				// TODO (EL, 2016-07-10): fix JSON format here; check how to implement this with regard to MediaType.
				.put(Entity.json(type), Response.class);
	}

	@Override
	public void delete(String pathToResourceId) {
		String pathWithResourceId = getPath() + '/' + pathToResourceId;
		response = getWebTarget()
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
				+ ", path=" + getPath()
				+ ", mediaType=" + getMediaType()
				+ "]";
	}
}
