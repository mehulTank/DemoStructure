package com.automobile.service.mvp.ForgotPsw;

import com.automobile.service.model.Forgot.ForgotResponseModel;
import com.automobile.service.model.Register.RegisterResponseModel;
import com.automobile.service.mvp.Register.RegisterScreen;
import com.automobile.service.mvp.Register.RegisterScreenPresenter;
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
 * Class : ForgotScreenPresenter
 */


public class ForgotScreenPresenter implements ForgotScreen.Presenter {

    public Retrofit retrofit;
    ForgotScreen.View mView;

    @Inject
    public ForgotScreenPresenter(Retrofit retrofit, ForgotScreen.View mView) {
        this.retrofit = retrofit;
        this.mView = mView;
    }


    @Override
    public void doForgot(String emailId) {


        retrofit.create(ForgotScreenPresenter.ForgotScreenService.class).doForgot(emailId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<ForgotResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ForgotResponseModel responseBase) {
                        mView.showResponse(responseBase);
                    }


                });
    }


    public interface ForgotScreenService {
        @FormUrlEncoded
        @POST(WsConstants.METHOD_FORGOT_PASSWORD)
        Observable<ForgotResponseModel> doForgot( @Field(ParamsConstans.PARAM_EMAIL_ID) String email);


    }


}
