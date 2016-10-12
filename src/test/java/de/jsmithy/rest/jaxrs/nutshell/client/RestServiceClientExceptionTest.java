package de.jsmithy.rest.jaxrs.nutshell.client;

import org.junit.Test;

/**
 * @author Erik Lotz
 * @since 2016-07-24
 * 
 */
public class RestServiceClientExceptionTest {

	@Test(expected=IllegalArgumentException.class)
	public void createInstanceWithIllegalNullRestServiceClient() {
		new RestServiceClientException(null);
	}
}
