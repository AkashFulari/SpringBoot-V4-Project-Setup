package com.akashf.springv4.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnHelloMessage() throws Exception {
		mockMvc.perform(get("/hello")
				.header("Accept-Language", "en"))
				.andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(content().string("Welcome Akash Fulari"));
	}

	@Test
	void shouldReturnExpectedJsonResponse() throws Exception {
		mockMvc.perform(get("/token/latest"))
				.andExpect(status().isOk())
				.andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
				.andExpect(content().contentTypeCompatibleWith(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.info.token").exists());
	}

}
