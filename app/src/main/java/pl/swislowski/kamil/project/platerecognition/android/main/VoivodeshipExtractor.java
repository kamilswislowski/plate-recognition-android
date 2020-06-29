package pl.swislowski.kamil.project.platerecognition.android.main;

public class VoivodeshipExtractor {
    private static final String VOIVODESHIP = "Województwo";

    public String extract(String string) {
        int index = string.indexOf(VOIVODESHIP);
        return string.substring(index);
    }
}
