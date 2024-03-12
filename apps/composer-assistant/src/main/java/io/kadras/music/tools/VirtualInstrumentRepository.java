package io.kadras.music.tools;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;

@Repository
class VirtualInstrumentRepository {

    private static final Map<String, String> virtualInstruments = Map.of(
            "piano", "NI Noire Piano",
            "strings", "Nucleus String",
            "brass", "Nucleus Brass",
            "woodwinds", "Nucleus Woodwinds",
            "violin", "Taylor Davis Violin",
            "cello", "Tina Guo Cello",
            "percussions", "Darwin Percussions",
            "drones", "Nucleus Drones",
            "harp", "Lumina Harp"
    );

    String getVstForInstrument(String instrument) {
        String vst = virtualInstruments.get(instrument);
        return Objects.requireNonNullElse(vst, "unavailable");
    }

}
