package pl.swislowski.kamil.project.platerecognition.android.service;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

public class RecognitionRegistrationPlateResultService {

    private static final String TAG = "RecognitionRegistrationPlateResultService";

    @SuppressLint("LongLogTag")
    public RegistrationPlateModel cameraResult(Bitmap imageBitmap) {

        Log.i(TAG, "Getting camera result ...");

        RegistrationPlateModel registrationPlateModel = null;


        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bs = null;

        try {
            bos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            bs = new ByteArrayInputStream(bitmapdata);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "_ANDROID_" + timeStamp;
//                registrationPlateModel =
//                        RecognitionRegistrationPlateService.recognition(bs, imageFileName);

//                Log.i(TAG, "######RegistrationPlateModel : " + registrationPlateModel);
            RecognitionRegistrationPlateAsyncTask recognitionRegistrationPlateAsyncTask = new RecognitionRegistrationPlateAsyncTask(bs, imageFileName);
            AsyncTask<Void, Void, RegistrationPlateModel> execute = recognitionRegistrationPlateAsyncTask.execute();
            RegistrationPlateModel registrationPlateModelAsyncTask = execute.get();
            Log.i(TAG, "Recognition() = " + registrationPlateModelAsyncTask);
            return registrationPlateModelAsyncTask;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, Arrays.toString(e.getStackTrace()));
//            } catch (ExecutionException e) {
//                Log.e(TAG, e.getMessage());
//                Log.e(TAG, Arrays.toString(e.getStackTrace()));
        } finally {
            if (bs != null) {
                try {
                    bs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
