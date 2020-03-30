package pl.swislowski.kamil.project.platerecognition.android.service;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

public class RecognitionRegistrationPlateAsyncTask extends AsyncTask<Void, Void, RegistrationPlateModel> {

    private InputStream inputStream;
    private String fileName;
    private String TAG = "RecognitionRegistrationPlateAsyncTask";

    public RecognitionRegistrationPlateAsyncTask(InputStream inputStream, String fileName) {
        this.inputStream = inputStream;
        this.fileName = fileName;
    }

    @Override
    protected RegistrationPlateModel doInBackground(Void... voids) {
        Log.i(TAG, "Doing in background");
        try {
            RegistrationPlateModel registrationPlateModel = RecognitionRegistrationPlateService.recognition(inputStream, fileName);
            Log.i(TAG, "" + registrationPlateModel);
            try {
                Document doc = Jsoup.connect("http://rejestracje.website.media.pl/?word="
                        + registrationPlateModel.getRegistrationNumber()).get();
                Elements result = doc.getElementsByClass("result");
                List<String> strings = result.eachText();
                if (strings != null && strings.size() > 0) {
                    Log.i(TAG, "############ " + strings.get(0));
//                    voivodeshipTextView.setText(strings.get(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "Doing in background : " + registrationPlateModel);
            return registrationPlateModel;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
