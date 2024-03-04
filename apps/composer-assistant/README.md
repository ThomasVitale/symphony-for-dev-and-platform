# Composer Assistant

AI assistant for music composers.

## Running the application

The application relies on an OpenAI API for providing LLMs and on the Weaviate vector store, provided as a dev service.

First, make sure you have an OpenAI account. Then, define an environment variable with the OpenAI API Key associated
to your OpenAI account as the value.

```shell
export LANGCHAIN4J_OPENAI_CLIENT_API_KEY=<INSERT KEY HERE>
```

### Dev Services with Docker Compose

Run the Spring Boot application as follows to start the Weaviate dev service via Docker Compose.

```shell
./gradlew bootRun
```

### Dev Services with Testcontainers

Run the Spring Boot application as follows to start the Weaviate dev service via Testcontainers.

```shell
./gradlew bootTestRun
```

## Calling the application

You can now call the application that will use OpenAI and _gpt-3.5-turbo_ by default and answer questions
based on the information loaded at startup time. This example uses [httpie](https://httpie.io) to send HTTP requests.

```shell
http :8080/chat message="Who is Clara?"
```
