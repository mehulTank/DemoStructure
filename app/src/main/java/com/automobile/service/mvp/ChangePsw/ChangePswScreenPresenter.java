package com.automobile.service.mvp.ChangePsw;

import com.automobile.service.model.ChangePsw.ChangePswResponseModel;
import com.automobile.service.model.Forgot.ForgotResponseModel;
import com.automobile.service.mvp.ForgotPsw.ForgotScreen;
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
 * Class : ChangePswScreenPresenter
 * Created Date :
 */


public class ChangePswScreenPresenter implements ChangePswScreen.Presenter {

    public Retrofit retrofit;
    ChangePswScreen.View mView;

    @Inject
    public ChangePswScreenPresenter(Retrofit retrofit, ChangePswScreen.View mView) {
        this.retrofit = retrofit;
        this.mView = mView;
    }

    @Override
    public void doChangePsw(String userID, String oldPsw, String newPsw) {


        retrofit.create(ChangePswScreenPresenter.ChangePswScreenService.class).doChangePsw(userID,oldPsw,newPsw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<ChangePswResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ChangePswResponseModel responseBase) {
                        mView.showResponse(responseBase);
                    }


                });


    }


    public interface ChangePswScreenService {
        @FormUrlEncoded
        @POST(WsConstants.METHOD_CHANGE_PSW)
        Observable<ChangePswResponseModel> doChangePsw(@Field(ParamsConstans.PARAM_USERID) String userid,
                                                 @Field(ParamsConstans.PARAM_OLD_PSW) String oldPsw,
                                                 @Field(ParamsConstans.PARAM_NEW_PSW) String newPsw);


    }


}
