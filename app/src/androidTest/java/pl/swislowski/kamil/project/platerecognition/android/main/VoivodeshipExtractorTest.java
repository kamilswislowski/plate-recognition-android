package pl.swislowski.kamil.project.platerecognition.android.main;

import org.junit.Test;

import static org.junit.Assert.*;

public class VoivodeshipExtractorTest {
    private static final String SEARCH_RESULT = "Szukana fraza: SB8 Województwo śląskie Bielsko-Biała";
    private static final String EXPECTED_VOIVODESHIP_RESULT = "Województwo śląskie Bielsko-Biała";

    @Test
    public void extract() {
        //given:
        VoivodeshipExtractor voivodeshipExtractor = new VoivodeshipExtractor();
        //when:
        String extractedString = voivodeshipExtractor.extract(SEARCH_RESULT);
        //then:
        assertEquals("Strings aren't equal.", EXPECTED_VOIVODESHIP_RESULT, extractedString);
    }
}