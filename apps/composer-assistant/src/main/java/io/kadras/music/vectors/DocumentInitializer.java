package io.kadras.music.vectors;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
@Profile("local")
class DocumentInitializer {

    private final DocumentLoader documentLoader;

    DocumentInitializer(DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
    }

    @PostConstruct
    void run() throws FileNotFoundException {
        documentLoader.loadDocuments();
    }

}
