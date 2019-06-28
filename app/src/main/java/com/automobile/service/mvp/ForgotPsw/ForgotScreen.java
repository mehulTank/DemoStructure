package com.automobile.service.mvp.ForgotPsw;

import com.automobile.service.model.Forgot.ForgotResponseModel;
import com.automobile.service.model.Register.RegisterResponseModel;

public interface ForgotScreen
{
    interface View
    {
        void showResponse(ForgotResponseModel responseBase);

        void showError(String message);


    }

    interface Presenter
    {
        void doForgot(String emailId);

    }
}
