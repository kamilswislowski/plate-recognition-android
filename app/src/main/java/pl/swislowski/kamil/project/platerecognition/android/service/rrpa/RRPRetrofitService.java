package pl.swislowski.kamil.project.platerecognition.android.service.rrpa;

import okhttp3.MultipartBody;
import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RRPRetrofitService {
    @Multipart
    @POST("plate-recognition/recognize")
    Call<RegistrationPlateModel> recognizeAsync(@Part MultipartBody.Part filePart);
}
