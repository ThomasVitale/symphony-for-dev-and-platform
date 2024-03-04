# Composer Assistant

AI assistant for music composers.

## Running the application

The application relies on Ollama for providing LLMs and on the Weaviate vector store, provided as a dev service.
You can run the native Ollama app locally on your laptop, or rely on the dev service provided by Testcontainers in Spring Boot.

### Dev Services with Docker Compose

Run the Spring Boot application as follows to use native Ollama and start the Weaviate dev service via Docker Compose.

```shell
./gradlew bootRun
```

### Dev Services with Testcontainers

Run the Spring Boot application as follows to start the Ollama and Weaviate dev services via Testcontainers.

```shell
./gradlew bootTestRun
```

## Calling the application

You can now call the application that will use Ollama and _llama2_ by default and answer questions
based on the information loaded at startup time. This example uses [httpie](https://httpie.io) to send HTTP requests.

```shell
http :8080/chat message="Who is Clara?"
```
