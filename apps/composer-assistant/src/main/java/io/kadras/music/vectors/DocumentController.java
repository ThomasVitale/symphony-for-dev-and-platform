package io.kadras.music.vectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class DocumentController {

    private final DocumentLoader documentLoader;

    DocumentController(DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
    }

    @PostMapping("/api/docs")
    void loadDocuments() throws FileNotFoundException {
        documentLoader.loadDocuments();
    }

}
