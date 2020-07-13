package pl.swislowski.kamil.project.platerecognition.android.service;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

public class RecognitionRegistrationPlateAsyncTask extends AsyncTask<InputStream, Void, RegistrationPlateModel> {
    private static final Logger LOGGER = Logger.getLogger(RecognitionRegistrationPlateAsyncTask.class.getName());
    private String TAG = "RRPAsyncTask";

    @Override
    protected RegistrationPlateModel doInBackground(InputStream... inputStreams) {
        LOGGER.info("doInBackground...");
        LOGGER.info("doInBackground params: " + inputStreams);

        try {
            InputStream inputStream = inputStreams[0];
            RegistrationPlateModel registrationPlateModel = RecognitionRegistrationPlateService.recognitionAsync(inputStream);

            RecognitionRegistrationPlateRepository recognitionRegistrationPlateRepository = new RecognitionRegistrationPlateRepository();
            String locationLabel = recognitionRegistrationPlateRepository.findByRegistrationNumber(registrationPlateModel);
            registrationPlateModel.setLocationLabel(locationLabel);

            LOGGER.info("doInBackground result: " + registrationPlateModel);
            return registrationPlateModel;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
