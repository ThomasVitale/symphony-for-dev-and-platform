package io.kadras.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.weaviate.WeaviateContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestComposerAssistantApplication {

	@Bean
	@RestartScope
	@ServiceConnection
	WeaviateContainer weaviate() {
		return new WeaviateContainer("semitechnologies/weaviate:1.24.1");
	}

	@Bean
	@RestartScope
	@ServiceConnection
	@Profile("ollama")
	OllamaContainer ollama() {
		return new OllamaContainer(DockerImageName.parse("ghcr.io/thomasvitale/ollama-llama2")
				.asCompatibleSubstituteFor("ollama/ollama"));
	}

	public static void main(String[] args) {
		SpringApplication.from(ComposerAssistantApplication::main).with(TestComposerAssistantApplication.class).run(args);
	}

}
