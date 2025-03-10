package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.yaksha.assignment.MyController;

@WebMvcTest(MyController.class)
public class MyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	// Test case for unauthorized access to the /admin endpoint
	@Test
	public void testAdminEndpointUnauthorized() throws Exception {
		// Simulate a request to the /admin endpoint without proper authentication (user
		// should not have access to this)
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/admin").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		// Perform the request and capture the response status
		String responseContent = mockMvc.perform(requestBuilder).andExpect(status().isUnauthorized()) // Expect 401
																										// Unauthorized
																										// status for
																										// unauthenticated
																										// user
				.andReturn().getResponse().getContentAsString();

		// Use yakshaAssert to verify the response status (we expect it to be
		// "Unauthorized")
		yakshaAssert(currentTest(), responseContent.isEmpty() ? "true" : "false", businessTestFile);
	}

	// Test case for unauthorized access to the /user endpoint
	@Test
	public void testUserEndpointUnauthorized() throws Exception {
		// Simulate a request to the /user endpoint without proper authentication (admin
		// should not have access)
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		// Perform the request and capture the response status
		String responseContent = mockMvc.perform(requestBuilder).andExpect(status().isUnauthorized()) // Expect 401
																										// Unauthorized
																										// status for
																										// unauthenticated
																										// user
				.andReturn().getResponse().getContentAsString();

		// Use yakshaAssert to verify the response status (we expect it to be
		// "Unauthorized")
		yakshaAssert(currentTest(), responseContent.isEmpty() ? "true" : "false", businessTestFile);
	}

	// Test case for accessing the /user endpoint with correct credentials
	@Test
	public void testUserEndpointWithAuthentication() throws Exception {
		// Simulate a valid request to the /user endpoint with Basic Authentication
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user")
				.header("Authorization", "Basic " + encodeCredentials("user", "password"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		// Perform the request and assert the response
		String responseContent = mockMvc.perform(requestBuilder).andExpect(status().isOk()) // Check that the response
																							// status is 200 OK
				.andExpect(content().string("Hello, User!")) // Check that the response content matches "Hello, User!"
				.andReturn().getResponse().getContentAsString();

		// Use yakshaAssert to check the response content
		yakshaAssert(currentTest(), responseContent.equals("Hello, User!") ? "true" : "false", businessTestFile);
	}

	// Test case for accessing the /admin endpoint with correct credentials
	@Test
	public void testAdminEndpointWithAuthentication() throws Exception {
		// Simulate a valid request to the /admin endpoint with Basic Authentication
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/admin")
				.header("Authorization", "Basic " + encodeCredentials("admin", "admin"))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		// Perform the request and assert the response
		String responseContent = mockMvc.perform(requestBuilder).andExpect(status().isOk()) // Check that the response
																							// status is 200 OK
				.andExpect(content().string("Hello, Admin!")) // Check that the response content matches "Hello, Admin!"
				.andReturn().getResponse().getContentAsString();

		// Use yakshaAssert to check the response content
		yakshaAssert(currentTest(), responseContent.equals("Hello, Admin!") ? "true" : "false", businessTestFile);
	}

	// Helper method to encode credentials to base64 (Basic Authentication)
	private String encodeCredentials(String username, String password) {
		String credentials = username + ":" + password;
		return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
	}
}
