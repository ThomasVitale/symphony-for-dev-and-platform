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

    @PostMapping("/chat")
    String chat(@RequestBody VideoInfo videoInfo) {
        return composerAgent.answer(videoInfo.description);
    }

    record VideoInfo(String description) {}

}
