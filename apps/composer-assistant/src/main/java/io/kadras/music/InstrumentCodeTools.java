package io.kadras.music;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstrumentCodeTools {

    @Autowired
    private InstrumentStore instrumentStore;

    @Tool
    public String getCodeForInstrument(Instrument instrument) throws InstumentNotFoundException {
       return instrumentStore.getCodeForInstrument(instrument);
    }

    @Tool
    public Instrument getInstrumentForCode(String code) throws CodeNotFoundException {
        return instrumentStore.getInstrumentForCode(code);
    }
}
