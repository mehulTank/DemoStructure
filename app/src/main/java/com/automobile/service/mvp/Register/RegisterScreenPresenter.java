package com.automobile.service.mvp.Register;

import com.automobile.service.model.Register.RegisterResponseModel;
import com.automobile.service.util.ParamsConstans;
import com.automobile.service.util.WsConstants;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Class : RegisterScreenPresenter
 */


public class RegisterScreenPresenter implements RegisterScreen.Presenter {

    public Retrofit retrofit;
    RegisterScreen.View mView;

    @Inject
    public RegisterScreenPresenter(Retrofit retrofit, RegisterScreen.View mView) {
        this.retrofit = retrofit;
        this.mView = mView;
    }


    @Override
    public void doRegister(String username, String password, String token, String emailId, String phone) {


        retrofit.create(RegisterScreenService.class).doRegister(username, password, token, emailId, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<RegisterResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(RegisterResponseModel responseBase) {
                        mView.showResponse(responseBase);
                    }


                });
    }


    public interface RegisterScreenService {
        @FormUrlEncoded
        @POST(WsConstants.METHOD_REGISTER)
        Observable<RegisterResponseModel> doRegister(@Field(ParamsConstans.PARAM_USERNAME) String userName,
                                                     @Field(ParamsConstans.PARAM_PASSWORD) String password,
                                                     @Field(ParamsConstans.PARAM_DEVICE_TOKEN) String token,
                                                     @Field(ParamsConstans.PARAM_EMAIL_ID) String email,
                                                     @Field(ParamsConstans.PARAM_PHONE) String phone);


    }


}
