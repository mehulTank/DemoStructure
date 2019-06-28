package com.automobile.service.mvp.Login;

import com.automobile.service.util.CustomScope;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginScreenModule
{
    private final LoginScreen.View mView;


    public LoginScreenModule(LoginScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    @CustomScope
    LoginScreen.View providesMainScreenContractView() {
        return mView;
    }
}