package de.jsmithy.rest.jaxrs.nutshell.client;

/**
 * Signals any exceptional situation while processing a RestServiceClient.
 * 
 * @author Erik Lotz
 * @since 2016-07-24
 * 
 */
public class RestServiceClientException extends RuntimeException {
	private static final long serialVersionUID = 3653367933177581941L;

	public RestServiceClientException(RestServiceClient restServiceClient) {
		if (restServiceClient == null) {
			throw new IllegalArgumentException("Argument [restServiceClient] must not be 'null'.");
		}
	}
}
