package de.jsmithy.rest.jaxrs.nutshell.client;

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
	JSON (MediaType.APPLICATION_JSON_TYPE),

	/**
	 * Use this media type if the content should be of {@code application/xml}.
	 */
	XML (MediaType.APPLICATION_XML_TYPE);
	
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
}
