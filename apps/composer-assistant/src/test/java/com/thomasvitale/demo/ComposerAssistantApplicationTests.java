package com.thomasvitale.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@EnabledIfEnvironmentVariable(named = "LANGCHAIN4J_OPENAI_CLIENT_API_KEY", matches = ".*")
@SpringBootTest
@Import(TestComposerAssistantApplication.class)
class ComposerAssistantApplicationTests {

	@Test
	void contextLoads() {
	}

}
