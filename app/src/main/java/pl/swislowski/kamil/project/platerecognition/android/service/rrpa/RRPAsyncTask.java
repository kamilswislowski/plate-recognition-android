package pl.swislowski.kamil.project.platerecognition.android.service.rrpa;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

public class RRPAsyncTask extends AsyncTask<InputStream, Void, RegistrationPlateModel> {
    private static final Logger LOGGER = Logger.getLogger(RRPAsyncTask.class.getName());
    private String TAG = "RRPAsyncTask";

    @Override
    protected RegistrationPlateModel doInBackground(InputStream... inputStreams) {
        LOGGER.info("doInBackground...");
        LOGGER.info("doInBackground params: " + inputStreams);

        try {
            InputStream inputStream = inputStreams[0];
            RegistrationPlateModel registrationPlateModel = RRPService.recognitionAsync(inputStream);

            RRPRepository rrpRepository = new RRPRepository();
            String locationLabel = rrpRepository.findByRegistrationNumber(registrationPlateModel);
            registrationPlateModel.setLocationLabel(locationLabel);

            LOGGER.info("doInBackground result: " + registrationPlateModel);
            return registrationPlateModel;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
