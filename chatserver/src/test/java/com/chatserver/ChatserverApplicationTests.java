package com.chatserver;

import com.chatserver.persistence.User;
import com.chatserver.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue
		;

@SpringBootTest
class ChatserverApplicationTests {
	@Autowired
	private AuthenticationService authenticationService;

	@Test
	public void testUserLogIn(){
		User testUser = new User("test1", "123456789");

		boolean logInResult = authenticationService.logIn(testUser.getUsername(), testUser.getPassword());
		assertTrue("Logged in successfully", logInResult);

		boolean containsTestUser  = authenticationService.getLoggedInUsers().contains(testUser);
		assertTrue("Contains test user", containsTestUser);
	}

}
