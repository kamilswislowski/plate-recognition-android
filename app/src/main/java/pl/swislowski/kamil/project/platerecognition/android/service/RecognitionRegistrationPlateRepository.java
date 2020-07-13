package pl.swislowski.kamil.project.platerecognition.android.service;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecognitionRegistrationPlateRepository {
    //TODO: Opcjonalnie zwracać Optionala zamiast pustego stringa.
    public String findByRegistrationNumber(RegistrationPlateModel registrationPlateModel) throws IOException {
        Document doc = Jsoup.connect("http://rejestracje.website.media.pl/?word="
                + registrationPlateModel.getRegistrationNumber()).get();
        Elements result = doc.getElementsByClass("result");
        List<String> strings = result.eachText();
        if (strings != null && strings.size() > 0) {
            String locationLabel = strings.get(0);
            Log.i(TAG, "#### Voivodeship result : " + locationLabel);
//                    voivodeshipTextView.setText(strings.get(0));
            return locationLabel;
        }
        return "";
    }
}
