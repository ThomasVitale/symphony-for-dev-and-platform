package io.kadras.music.vectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class DocumentController {

    private final DocumentInitializer documentInitializer;

    DocumentController(DocumentInitializer documentInitializer) {
        this.documentInitializer = documentInitializer;
    }

    @PostMapping("/api/docs")
    void loadDocuments() throws FileNotFoundException {
        documentInitializer.loadDocuments();
    }

}
