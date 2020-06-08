package pl.swislowski.kamil.project.platerecognition.android.service.rrpa;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

public class RRPService {
    private static final String TAG = "RRPService";
    public static final String SERVER_URL = "http://10.0.2.2:8080/";
    public static final String HEROKU_SERVER_URL = "https://plate-recognition-spring.herokuapp.com/";

    public static RegistrationPlateModel recognitionAsync(InputStream inputStream) throws IOException {
        Log.i(TAG, "#### Starting recognitionAsync method ...");

        byte[] buffor = new byte[512];
        ByteArrayOutputStream upload = new ByteArrayOutputStream();
        while (inputStream.read(buffor) != -1) {
            upload.write(buffor);
        }

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                "upload",
                "file.jpg",
                RequestBody.create(MediaType.parse("image/*"), upload.toByteArray()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RRPRetrofitService retrofitService = retrofit.create(RRPRetrofitService.class);
        Call<RegistrationPlateModel> call = retrofitService.recognizeAsync(filePart);

        RegistrationPlateModel body = call.execute().body();
//        call.enqueue(new RRPRetrofitCallback());
        Log.i(TAG, "#### recognitionAsync body: " + body);

        return body;
    }
}
