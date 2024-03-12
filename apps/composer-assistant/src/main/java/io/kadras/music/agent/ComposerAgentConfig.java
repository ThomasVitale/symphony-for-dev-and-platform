package io.kadras.music.agent;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.kadras.music.tools.VirtualInstrumentTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ComposerAgentConfig {

    @Bean
    ComposerAgent documentAgent(ChatLanguageModel chatLanguageModel, EmbeddingModel embeddingModel,
                                EmbeddingStore<TextSegment> embeddingStore, VirtualInstrumentTools virtualInstrumentTools) {

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(7)
                .minScore(0.5)
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        return AiServices.builder(ComposerAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(contentRetriever)
                .chatMemory(chatMemory)
                .tools(virtualInstrumentTools)
                .build();
    }

}
