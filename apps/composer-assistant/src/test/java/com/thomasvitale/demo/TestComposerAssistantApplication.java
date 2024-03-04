package com.thomasvitale.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.weaviate.WeaviateContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestComposerAssistantApplication {

	@Bean
	@RestartScope
	@ServiceConnection
	WeaviateContainer weaviate() {
		return new WeaviateContainer("semitechnologies/weaviate:1.23.10");
	}

	public static void main(String[] args) {
		SpringApplication.from(ComposerAssistantApplication::main).with(TestComposerAssistantApplication.class).run(args);
	}

}
