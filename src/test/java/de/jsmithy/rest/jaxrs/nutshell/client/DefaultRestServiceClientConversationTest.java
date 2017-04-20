package de.jsmithy.rest.jaxrs.nutshell.client;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

/**
 * Test case for the conversational aspects of type DefaultRestServiceClientTest.
 * 
 * @author Erik Lotz
 * @since 2017-04-12
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultRestServiceClientConversationTest {
	private DefaultRestServiceClient sutSpy = Mockito.spy(DefaultRestServiceClient.class);;
	
	@Before
	public void setUp() {
		Client clientMock = Mockito.mock(Client.class);
		Mockito.doReturn(clientMock).when(sutSpy).createRestClient();
	}

	@After
	public void tearDown() {
		if (sutSpy.isConversationStarted()) {
			sutSpy.closeConversation();
		}
	}
	
	@Test
	public void testIsConversationStarted_after_creation() {
		boolean result = sutSpy.isConversationStarted();
		
		assertFalse("Conversation must not be started now!", result);
	}
	
	@Test
	public void testOpenConversation_success() {
		sutSpy.openConversation();
		
		boolean result = sutSpy.isConversationStarted();
		
		assertTrue("Conversation must be started now!", result);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testOpenConversation_when_it_has_been_already_openened() {
		sutSpy.openConversation();
		
		sutSpy.openConversation();
	}
	
	@Test
	public void testCloseConversation_success() {
		sutSpy.openConversation();
		
		sutSpy.closeConversation();
		
		boolean result = sutSpy.isConversationStarted();
		assertFalse("Conversation must not be started now!", result);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCloseConversation_without_opening_it_before() {
		sutSpy.closeConversation();
	}
}
