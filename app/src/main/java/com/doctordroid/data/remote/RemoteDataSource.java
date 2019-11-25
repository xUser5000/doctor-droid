package com.doctordroid.data.remote;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource {

    private static RemoteDataSource ourInstance;

    private Api api;
    private final String BASE_URL = "https://api.infermedica.com/v2/";
    private Retrofit retrofit;

    public static RemoteDataSource getInstance() {
        if (ourInstance == null) ourInstance = new RemoteDataSource();
        return ourInstance;
    }

    private RemoteDataSource() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build();
        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }

    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter =
                        getInstance().retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }

        return error;
    }
}
