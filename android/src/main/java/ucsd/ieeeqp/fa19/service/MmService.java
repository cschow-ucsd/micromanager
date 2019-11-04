package ucsd.ieeeqp.fa19.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ucsd.ieeeqp.fa19.MmClientGoogleAuthToken;

public interface MmService {
    @POST("/protected")
    public Call<TestResponse> getProtected(
            @Body MmClientGoogleAuthToken token
    );
}
