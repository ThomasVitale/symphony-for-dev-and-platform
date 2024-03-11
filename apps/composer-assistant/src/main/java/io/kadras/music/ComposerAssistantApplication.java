package io.kadras.music;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
// TODO What to do with logging slf4j -> for platform

@SpringBootApplication
public class ComposerAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComposerAssistantApplication.class, args);
	}

	// TODO split in subbeans? where/how are Weaviate and OpenAI injected exactly?
	// TODO change to Mistral (key set in MISTRAL_AI_API_KEY system variable)
	@Bean
	ComposerAgent documentAgent(ChatLanguageModel chatLanguageModel, EmbeddingModel embeddingModel,
								EmbeddingStore<TextSegment> embeddingStore, InstrumentCodeTools instrumentCodeTools) throws IOException {
		// TODO add good descriptions for Thomas' instruments
		Path documentPath = ResourceUtils.getFile("classpath:documents/virtual-instruments-of-thomas.txt").toPath();
		DocumentParser documentParser = new TextDocumentParser();
		Document document = FileSystemDocumentLoader.loadDocument(documentPath, documentParser);

		EmbeddingStoreIngestor dataIngestor = EmbeddingStoreIngestor.builder()
				.embeddingStore(embeddingStore)
				.embeddingModel(embeddingModel)
				// TODO make per paragraph splitter
				.documentSplitter(DocumentSplitters.recursive(300, 10))
				.build();
		dataIngestor.ingest(document);

		ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
				.embeddingStore(embeddingStore)
				.embeddingModel(embeddingModel)
				// TODO tweak
				.maxResults(3)
				.minScore(0.5)
				.build();
		
		ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
		return AiServices.builder(ComposerAgent.class)
				.chatLanguageModel(chatLanguageModel)
				.contentRetriever(contentRetriever)
				.chatMemory(chatMemory)
				.tools(instrumentCodeTools)
				.build();
	}

}

@RestController
class ChatController {

	private final ComposerAgent composerAgent;

	ChatController(ComposerAgent composerAgent) {
		this.composerAgent = composerAgent;
	}
	
	@PostMapping("/chat")
	String chat(@RequestBody MusicPrompt prompt) {
		String videoDescription = prompt.message();
		// TODO use promptTemplate and SystemMessage

		String SystemMessage = "You are a friendly assistant to a music composer who's working on the soundtrack for a movie. Whenever you get a question, you should provide concise and short answers, and not more than 3 options. When providing composition tips, explain step-by-step. The composer uses the Logic Pro X software as DAW, so make sure you tips are contextual to that software. A vocal soloist is also available.";

		String instruction = "Can you recommend some chord progressions and virtual instruments to score a video " +
				"fragment of ";

		String engineeredPrompt = SystemMessage+ "\n\n" + instruction + videoDescription;

		return composerAgent.answer(engineeredPrompt);
	}

}

interface ComposerAgent {
	String answer(String prompt);
}

record MusicPrompt(String message) {}
