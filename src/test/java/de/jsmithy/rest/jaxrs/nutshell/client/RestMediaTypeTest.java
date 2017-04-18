package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * 
 * @author Erik Lotz
 * @since 2017-04-13
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestMediaTypeTest {

	@Test
	public void testToString_JSON() {
		RestMediaType sut = RestMediaType.JSON;
		String expected = "JSON";
		
		String actual = sut.toString();
		
		assertEquals("String representation of 'JSON' is not correct!", expected, actual);
	}

	@Test
	public void testToString_XML() {
		RestMediaType sut = RestMediaType.XML;
		String expected = "XML";
		
		String actual = sut.toString();
		
		assertEquals("String representation of 'XML' is not correct!", expected, actual);
	}
	
	@Test
	public void testGetMediaType() {
		RestMediaType sut = RestMediaType.JSON;
		MediaType expected = MediaType.APPLICATION_JSON_TYPE;
		
		MediaType actual = sut.getMediaType();
		
		assertEquals("[mediaType] is not correct!", expected, actual);
	}
	
	@Test
	public void testGetEntity_JSON() {
		RestMediaType sut = RestMediaType.JSON;
		MediaType expected = MediaType.APPLICATION_JSON_TYPE;
		JsonPlaceholderPost entityType = getJsonPlaceholderPost();
		
		Entity<JsonPlaceholderPost> entity = sut.getEntity(entityType);
		
		boolean result = entity.getEntity() instanceof JsonPlaceholderPost;
		assertTrue("Entity has not been created correctly!", result);

		MediaType actual = entity.getMediaType();
		assertEquals("Entity has not been created correctly!", expected, actual);
	}

	@Test
	public void testGetEntity_XML() {
		RestMediaType sut = RestMediaType.XML;
		MediaType expected = MediaType.APPLICATION_XML_TYPE;
		JsonPlaceholderPost entityType = getJsonPlaceholderPost();
		
		Entity<JsonPlaceholderPost> entity = sut.getEntity(entityType);
		
		boolean result = entity.getEntity() instanceof JsonPlaceholderPost;
		assertTrue("Entity has not been created correctly!", result);

		MediaType actual = entity.getMediaType();
		assertEquals("Entity has not been created correctly!", expected, actual);
	}

	private JsonPlaceholderPost getJsonPlaceholderPost() {
		JsonPlaceholderPost result = new JsonPlaceholderPost();
		result.setId(1);
		result.setTitle("Some title");
		result.setBody("Some body");
		
		return result;
	}
}
