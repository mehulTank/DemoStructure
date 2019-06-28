package com.automobile.service.mvp.ChangePsw;

import com.automobile.service.model.ChangePsw.ChangePswResponseModel;
import com.automobile.service.model.Forgot.ForgotResponseModel;

public interface ChangePswScreen
{
    interface View
    {
        void showResponse(ChangePswResponseModel responseBase);

        void showError(String message);


    }

    interface Presenter
    {
        void doChangePsw(String userID,String oldPsw,String newPsw);

    }
}
