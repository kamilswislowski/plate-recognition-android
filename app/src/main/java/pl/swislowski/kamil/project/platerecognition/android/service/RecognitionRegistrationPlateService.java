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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecognitionRegistrationPlateService {

    private static final String TAG = "RecognitionRegistrationPlateService";
    public static final String SERVER_URL = "http://10.0.2.2:8080/";
    public static final String HEROKU_SERVER_URL = "https://plate-recognition-spring.herokuapp.com/";

    @SuppressLint("LongLogTag")
    public static RegistrationPlateModel recognition(InputStream inputStream, String fileName) throws IOException {

        Log.i(TAG, "########Starting recognition method ...");

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
                .baseUrl(HEROKU_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecognitionRegistrationPlateRetrofitService recognitionRegistrationPlateRetrofitService =
                retrofit.create(RecognitionRegistrationPlateRetrofitService.class);

        Call<RegistrationPlateModel> call = recognitionRegistrationPlateRetrofitService.recognize(filePart);

        return call.execute().body();
    }
}
