package io.kadras.music.vectors;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.nio.file.Path;

@Component
class DocumentLoader {

    private static final Logger log = LoggerFactory.getLogger(DocumentLoader.class);

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    DocumentLoader(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    void loadDocuments() throws FileNotFoundException {
        log.info("Loading text files as TextSegments");
        Path documentPath = ResourceUtils.getFile("classpath:documents/instrument-emotions.txt").toPath();
        DocumentParser documentParser = new TextDocumentParser();
        DocumentByParagraphSplitter documentSplitter = new DocumentByParagraphSplitter(300, 10);
        Document document = FileSystemDocumentLoader.loadDocument(documentPath, documentParser);

        EmbeddingStoreIngestor dataIngestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .documentSplitter(documentSplitter)
                .build();

        log.info("Creating and storing Embeddings from TextSegments");
        dataIngestor.ingest(document);

        log.info("Embeddings created and stored successfully");
    }

}
