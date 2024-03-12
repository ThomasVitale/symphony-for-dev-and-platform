package io.kadras.music.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class VirtualInstrumentTools {

    private final VirtualInstrumentRepository virtualInstrumentRepository;

    VirtualInstrumentTools(VirtualInstrumentRepository virtualInstrumentRepository) {
        this.virtualInstrumentRepository = virtualInstrumentRepository;
    }

    @Tool
    public String getVirtualInstrumentNameFromInstrument(String instrument) {
       return virtualInstrumentRepository.getVstForInstrument(instrument);
    }

}
