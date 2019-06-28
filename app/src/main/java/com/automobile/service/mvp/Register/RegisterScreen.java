package com.automobile.service.mvp.Register;

import com.automobile.service.model.Login.LoginResponseModel;
import com.automobile.service.model.Register.RegisterResponseModel;

public interface RegisterScreen
{
    interface View
    {
        void showResponse(RegisterResponseModel responseBase);

        void showError(String message);


    }

    interface Presenter
    {
        void doRegister(String username, String password, String token,String emailId,String phone);

    }
}
