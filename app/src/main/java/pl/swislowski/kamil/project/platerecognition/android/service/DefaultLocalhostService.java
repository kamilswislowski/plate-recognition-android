package pl.swislowski.kamil.project.platerecognition.android.service;

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

public class DefaultLocalhostService {

    private static final String TAG = "DefaultLocalhostService";

    public static void recognition(InputStream inputStream) throws IOException {

        byte[] buffor = new byte[512];
        ByteArrayOutputStream upload = new ByteArrayOutputStream();
        while (inputStream.read(buffor) != -1) {
            upload.write(buffor);
        }

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                "upload",
                "fiat_tablice.jpg",
                RequestBody.create(MediaType.parse("image/*"), upload.toByteArray()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecognizeService recognizeService = retrofit.create(RecognizeService.class);

        Call<RegistrationPlateModel> call = recognizeService.uploadImage(filePart);

        call.enqueue(new Callback<RegistrationPlateModel>() {
            @Override
            public void onResponse(Call<RegistrationPlateModel> call, Response<RegistrationPlateModel> response) {
                Log.i(TAG, "#####On response.");
                RegistrationPlateModel body = response.body();
                Log.i(TAG, "####BODY : " + body);
            }

            @Override
            public void onFailure(Call<RegistrationPlateModel> call, Throwable t) {
                Log.i(TAG, "#####On failure : " + t);
            }
        });
    }

    public static void dummy() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
//                            .baseUrl("http://192.168.8.112:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LocalhostService localhostService = retrofit.create(LocalhostService.class);
            Call<RegistrationPlateModel> callback = localhostService.dummy();
            callback.enqueue(new Callback<RegistrationPlateModel>() {
                @Override
                public void onResponse(Call<RegistrationPlateModel> call, Response<RegistrationPlateModel> response) {
                    Log.i(TAG, "#####On response.");
                    RegistrationPlateModel body = response.body();
                    Log.i(TAG, "####BODY : " + body);
                }

                @Override
                public void onFailure(Call<RegistrationPlateModel> call, Throwable t) {
                    Log.i(TAG, "#####On failure : " + t);
                }
            });

            Log.i(TAG, "DUMMY METHOD INVOCATION.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
