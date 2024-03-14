package io.kadras.music.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.V;
import org.springframework.stereotype.Component;

@Component
public class VirtualInstrumentTools {

    private final VirtualInstrumentRepository virtualInstrumentRepository;

    VirtualInstrumentTools(VirtualInstrumentRepository virtualInstrumentRepository) {
        this.virtualInstrumentRepository = virtualInstrumentRepository;
    }

    @Tool("For each instrument, return the name of an available virtual instrument")
    public String getVirtualInstrumentNameFromInstrument(@V("instrument") String instrument) {
       return virtualInstrumentRepository.getVstForInstrument(instrument);
    }

}
