package com.myleads.android.retrovit;

import com.myleads.android.model.LeadModel;
import com.myleads.android.model.TokenModel;
import com.myleads.android.retrovit.pojo.GeneralResponseData;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface MyLeadsAPI {


    @POST("login")
    Observable<GeneralResponseData<TokenModel>> login(@QueryMap Map<String, String> login);

    @POST("register")
    Observable<GeneralResponseData<TokenModel>> register(@QueryMap Map<String, String> register);
    @POST("leads")
    Observable<GeneralResponseData<LeadModel>> createLead(@QueryMap Map<String, String> register);
    @GET("leads")
    Observable<GeneralResponseData<ArrayList<LeadModel>>> getLeads(@QueryMap Map<String, String> params);



}
