package de.jsmithy.rest.jaxrs.nutshell.client;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * @author Erik Lotz
 * @since 2016-07-24
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestServiceClientExceptionTest {

	@Test(expected=IllegalArgumentException.class)
	public void createInstanceWithIllegalNullRestServiceClient() {
		new RestServiceClientException(null);
	}
}
