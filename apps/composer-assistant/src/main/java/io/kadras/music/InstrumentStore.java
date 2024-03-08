package io.kadras.music;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// TODO add Thomas' real instruments and codes
enum Instrument {
    VIOLIN,
    FLUTE,
    GUITAR,
    PIANO,
    DRUMS,
    BAGPIPES
}
@Component
public class InstrumentStore {

    private static final Map<Instrument, String> instrumentCodeMap = new HashMap<>();

    static {
        instrumentCodeMap.put(Instrument.VIOLIN, "V123");
        instrumentCodeMap.put(Instrument.FLUTE, "F456");
        instrumentCodeMap.put(Instrument.GUITAR, "G789");
        instrumentCodeMap.put(Instrument.PIANO, "P012");
        instrumentCodeMap.put(Instrument.DRUMS, "D345");
        instrumentCodeMap.put(Instrument.BAGPIPES, "B345");
    }

    public static String getCodeForInstrument(Instrument instrument) throws InstumentNotFoundException {
        String code = instrumentCodeMap.get(instrument);
        if (instrument != null) {
            return code;
        }
        throw (new InstumentNotFoundException());
    }

    public static Instrument getInstrumentForCode(String code) throws CodeNotFoundException {
        for (Map.Entry<Instrument, String> entry : instrumentCodeMap.entrySet()) {
            if (entry.getValue().equals(code)) {
                return entry.getKey();
            }
        }
        throw (new CodeNotFoundException());
    }

}
