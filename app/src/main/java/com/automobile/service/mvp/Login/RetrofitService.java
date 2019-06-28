package com.automobile.service.mvp.Login;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService{
        @GET("/users")
        Call<ResponseBody> listRepos();//function to call api
    }