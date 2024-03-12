package io.kadras.music.agent;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ComposerAgentController {

    private final ComposerAgent composerAgent;

    ComposerAgentController(ComposerAgent composerAgent) {
        this.composerAgent = composerAgent;
    }

    @PostMapping("/api/compose")
    CompositionStrategy compose(@RequestBody SceneInfo sceneInfo) {
        var content = composerAgent.answer(sceneInfo.description);
        return new CompositionStrategy(content);
    }

    record SceneInfo(String description) {}

    record CompositionStrategy(String content) {}

}
