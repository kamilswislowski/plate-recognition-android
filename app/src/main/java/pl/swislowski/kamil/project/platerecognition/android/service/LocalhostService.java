package pl.swislowski.kamil.project.platerecognition.android.service;

import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LocalhostService {

    @GET("plate-recognition/dummy")
    Call<RegistrationPlateModel> dummy();
}
