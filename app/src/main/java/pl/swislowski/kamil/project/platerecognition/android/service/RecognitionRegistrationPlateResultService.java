package pl.swislowski.kamil.project.platerecognition.android.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import pl.swislowski.kamil.project.platerecognition.android.constants.Constants;

import static android.app.Activity.RESULT_OK;

public class RecognitionRegistrationPlateResultService {

    private static final String TAG = "RecognitionRegistrationPlateResultService";

    @SuppressLint("LongLogTag")
    public void cameraResult(int requestCode, int resultCode, Intent data){

        Log.i(TAG, "Getting camera result ...");

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "_ANDROID_" + timeStamp;
                RecognitionRegistrationPlateService.recognition(bs, imageFileName);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
