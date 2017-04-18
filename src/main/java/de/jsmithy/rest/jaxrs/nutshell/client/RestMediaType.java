package de.jsmithy.rest.jaxrs.nutshell.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * This type is an abstraction of {@code javax.ws.rs.core.MediaType}.
 * 
 * It is used to determine the content type of service invocations of
 * the {@link RestServiceClient}. Currently the supported types are
 * only JSON and XML.
 * 
 * @author Erik Lotz
 * @since 2017-04-13
 * 
 */
public enum RestMediaType {
	/**
	 * Use this media type if the content should be of {@code application/json}.
	 */
	JSON (MediaType.APPLICATION_JSON_TYPE) {
		@Override
		public <T> Entity<T> getEntity(T type) {
			return Entity.json(type);
		}
	},

	/**
	 * Use this media type if the content should be of {@code application/xml}.
	 */
	XML (MediaType.APPLICATION_XML_TYPE) {
		@Override
		public <T> Entity<T> getEntity(T type) {
			return Entity.xml(type);
		}
	};
	
	private final MediaType mediaType;
	
	RestMediaType(MediaType aType) {
		mediaType = aType;
	}
	
	/**
	 * Answers the {@link MediaType} of this instance.
	 * 
	 * @return The {@link MediaType} this instance is a wrapper for.
	 * 
	 */
	public MediaType getMediaType() {
		return mediaType;
	}

	/**
	 * Creates a JAX-RS {@link Entity} representation for the given type depending on the underlying {@link MediaType}
	 * which could be JSON, XML etc.
	 * 
	 * @param type A generic type that should be represented as a JAX-RS Entity.
	 * @return A JAX-RS Entity ({@link Entity}) representation of the given type.
	 * 
	 */
	public abstract <T> Entity<T> getEntity(T type);
}
