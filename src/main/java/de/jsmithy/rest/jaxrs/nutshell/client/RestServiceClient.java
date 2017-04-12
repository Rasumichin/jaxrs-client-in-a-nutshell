package de.jsmithy.rest.jaxrs.nutshell.client;

import java.net.URI;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	
	MediaType getMediaType();
	void setMediaType(MediaType mediaType);
	
	
	<T> void create(T type);

	<T> T read(Class<T> type);

	// TODO (Erik, 2016-07-09): Maybe we manage to implement this signature to avoid REST-Client dependency on the caller's side.
	// <T> List<T> readList(Class<T> aClass);
	
	<T> T read(GenericType<T> genericType);

	<T> void update(T type);

	void delete(String pathToResourceId);
}
