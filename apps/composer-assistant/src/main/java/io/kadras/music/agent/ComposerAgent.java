package io.kadras.music.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ComposerAgent {

    @SystemMessage("""
        You are a friendly assistant to a music composer who's working on the soundtrack for a movie.
        Whenever you get a question, you should provide concise and short answers.
        First, recommend three chord progressions. Then, provide a composition strategy step-by-step,
        with at most seven steps. For each instrument, include the virtual instrument name.
        Always include a vocal soloist in the composition.
    """)

    @UserMessage("""
        Explain the composition strategy step-by-step to score a video fragment of {{it}}.
    """)
    String answer(String description);
}
