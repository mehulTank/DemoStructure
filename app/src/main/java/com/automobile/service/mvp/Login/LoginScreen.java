package com.automobile.service.mvp.Login;

import com.automobile.service.model.Login.LoginModelData;

public interface LoginScreen {
    interface View {

        void showResponse(boolean availData, LoginModelData loginModelData);

        void showError(String message);


    }

    interface Presenter {
        void doLogin(String email, String password, String token, String user_udid_data);

        void doSocialLogin(String socialId, String token, String emailId, String username);


    }
}
