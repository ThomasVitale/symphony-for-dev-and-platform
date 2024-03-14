package io.kadras.music.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;

@Repository
class VirtualInstrumentRepository {

    private static final Logger logger = LoggerFactory.getLogger(VirtualInstrumentRepository.class);

    private static final Map<String, String> virtualInstruments = Map.of(
            "piano", "NI Noire Piano",
            "strings", "Albion String",
            "brass", "Nucleus Brass",
            "violin", "Taylor Davis Violin",
            "cello", "Tina Guo Cello",
            "percussions", "Darwin Percussions",
            "drones", "Nucleus Drones",
            "harp", "Lumina Harp"
    );

    String getVstForInstrument(String instrument) {
        logger.info("Retrieving virtual instrument for %s".formatted(instrument));
        String vst = virtualInstruments.get(instrument.toLowerCase());
        return Objects.requireNonNullElse(vst, "unavailable");
    }

}
