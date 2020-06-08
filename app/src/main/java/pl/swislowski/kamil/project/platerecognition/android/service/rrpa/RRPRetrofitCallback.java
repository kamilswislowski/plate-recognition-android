package pl.swislowski.kamil.project.platerecognition.android.service.rrpa;

import android.annotation.SuppressLint;
import android.util.Log;

import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class RRPRetrofitCallback implements Callback<RegistrationPlateModel> {
    private static final String TAG = "RecognitionRegistrationPlateRetrofitCallback";
    private RegistrationPlateModel registrationPlateModel;

    @Override
    public void onResponse(Call<RegistrationPlateModel> call, Response<RegistrationPlateModel> response) {
        Log.i(TAG, "On response : " + response.body());
        registrationPlateModel = response.body();
    }

    @Override
    public void onFailure(Call<RegistrationPlateModel> call, Throwable t) {
        Log.i(TAG, "On failure : " + t);
    }

    public RegistrationPlateModel getRegistrationPlateModel() {
        return registrationPlateModel;
    }
}
