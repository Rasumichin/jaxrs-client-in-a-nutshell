package de.jsmithy.rest.jaxrs.nutshell.client;

import java.net.URI;

import javax.ws.rs.core.*;

/**
 * Defines the necessary interactions to call REST services hiding the details of the JAX-RS client API.
 * 
 * @author Erik Lotz
 * @since 2016-07-02
 * 
 */
public interface RestServiceClient {
	void openConversation();
	void closeConversation();
	boolean isConversationStarted();

	URI getResourceUri();
	Response getResponse();
	
	String getPath();
	void setPath(String path);
	
	RestMediaType getMediaType();
	void setMediaType(RestMediaType mediaType);
	
	<T> void create(T type);

	<T> T read(Class<T> type);
	<T> T readList(GenericType<T> genericType);

	<T> void update(T type);

	void delete(String pathToResourceId);
}
