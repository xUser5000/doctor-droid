package com.doctordroid.data.remote;

import com.doctordroid.entity.remote.request.DiagnosisRequest;
import com.doctordroid.entity.remote.request.ParseRequest;
import com.doctordroid.entity.remote.response.ConditionResponse;
import com.doctordroid.entity.remote.response.DiagnosisResponse;
import com.doctordroid.entity.remote.response.ParseResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    @POST("parse")
    @Headers({
            "Content-Type: application/json",
            "Dev-Mode: true",
            "App-Id: 2221de61",
            "App-Key: b96184c47dd3f09ceca1f3a2cfad8531"
    })
    Single<Response<ParseResponse>> parseFreeText (@Body ParseRequest request);

    @POST("diagnosis")
    @Headers({
            "Content-Type: application/json",
            "Dev-Mode: true",
            "App-Id: 2221de61",
            "App-Key: b96184c47dd3f09ceca1f3a2cfad8531"
    })
    Single<Response<DiagnosisResponse>> diagnose (@Body DiagnosisRequest request);

    @GET("conditions/{id}")
    @Headers({
            "Content-Type: application/json",
            "Dev-Mode: true",
            "App-Id: 2221de61",
            "App-Key: b96184c47dd3f09ceca1f3a2cfad8531"
    })
    Single<Response<ConditionResponse>> getConditionInfo (@Path("id") String conditionId);

}
