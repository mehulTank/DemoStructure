package com.automobile.service.mvp.Login;

import android.util.Log;

import com.automobile.service.model.Login.LoginModelData;
import com.automobile.service.util.ParamsConstans;
import com.automobile.service.util.WsConstants;
import com.automobile.service.webservice.WSUtil;
import com.google.gson.Gson;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Class : LoginScreenPresenter
 */


public class LoginScreenPresenter implements LoginScreen.Presenter {

    public Retrofit retrofit;
    LoginScreen.View mView;

    @Inject
    public LoginScreenPresenter(Retrofit retrofit, LoginScreen.View mView) {
        this.retrofit = retrofit;
        this.mView = mView;
    }

    @Override
    public void doLogin(String service, String sent_email_data, String sent_password, String user_udid_data) {

        retrofit.create(LoginScreenPresenter.LoginScreenService.class).doLogin(service, sent_email_data, sent_password, user_udid_data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LoginModelData loginModelData = null;
                boolean isDataAvail = false;
                if (response.isSuccessful()) {
                    if (WSUtil.isDataAvaible(response.body().toString())) {

                        isDataAvail = true;
                        Gson gson = new Gson();
                        loginModelData = gson.fromJson(response.body().toString(), LoginModelData.class);
                        Log.d("----- user emai", loginModelData.getData().getUserEmail());

                    } else {
                        isDataAvail = false;
                        Log.d("----- user emai", "not succes message");
                    }

                }


                mView.showResponse(isDataAvail, loginModelData);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

   /*     retrofit.create(RetrofitService.class).doLogin(service, sent_email_data, sent_password,user_udid_data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Overridem
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response responseBase) {
                        mView.showResponse(responseBase);
                    }


                });*/
    }

    @Override
    public void doSocialLogin(String socialId, String token, String emailId, String username) {


        retrofit.create(LoginScreenService.class).doSocialLogin(socialId, token, emailId, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response responseBase) {
                        /*mView.showResponse(responseBase);*/
                    }


                });
    }


    public interface LoginScreenService {
        @FormUrlEncoded
        @POST(WsConstants.METHOD_LOGIN)
        Call<String> doLogin(@Field("service") String email,
                             @Field("sent_email_data") String sent_email_data,
                             @Field("sent_password") String sent_password,
                             @Field("user_udid_data") String user_udid_data);

        @FormUrlEncoded
        @POST(WsConstants.METHOD_SOCIAL_LOGIN)
        Observable<Response> doSocialLogin(@Field(ParamsConstans.PARAM_SOCIAL_ID) String socialId,
                                           @Field(ParamsConstans.PARAM_DEVICE_TOKEN) String token,
                                           @Field(ParamsConstans.PARAM_EMAIL_ID) String emailID,
                                           @Field(ParamsConstans.PARAM_USERNAME) String userName);


    }


}
