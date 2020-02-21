package pl.swislowski.kamil.project.platerecognition.android.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecognitionRegistrationPlateService {

    private static final String TAG = "RecognitionRegistrationPlateService";
    public static final String SERVER_URL = "http://10.0.2.2:8080/";

    public static void recognition(InputStream inputStream, String fileName) throws IOException {

        byte[] buffor = new byte[512];
        ByteArrayOutputStream upload = new ByteArrayOutputStream();
        while (inputStream.read(buffor) != -1) {
            upload.write(buffor);
        }

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                "upload",
                fileName,
                RequestBody.create(MediaType.parse("image/*"), upload.toByteArray()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecognitionRegistrationPlateRetrofitService recognitionRegistrationPlateRetrofitService =
                retrofit.create(RecognitionRegistrationPlateRetrofitService.class);

        Call<RegistrationPlateModel> call = recognitionRegistrationPlateRetrofitService.recognize(filePart);


        call.enqueue(new Callback<RegistrationPlateModel>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<RegistrationPlateModel> call, Response<RegistrationPlateModel> response) {
                RegistrationPlateModel body = response.body();
                Log.i(TAG, "On response : " + body);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<RegistrationPlateModel> call, Throwable t) {
                Log.i(TAG, "On failure : " + t);
            }
        });
    }
}
