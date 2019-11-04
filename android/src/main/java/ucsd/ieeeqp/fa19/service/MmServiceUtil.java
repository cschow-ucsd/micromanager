package ucsd.ieeeqp.fa19.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MmServiceUtil {
    public static final String BASE_URL = "http://localhost:8080";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static final MmService service = retrofit.create(MmService.class);
}