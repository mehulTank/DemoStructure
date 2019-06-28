package com.automobile.service.mvp.Register;

import com.automobile.service.mvp.Login.LoginScreen;
import com.automobile.service.util.CustomScope;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterScreenModule
{
    private final RegisterScreen.View mView;


    public RegisterScreenModule(RegisterScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    @CustomScope
    RegisterScreen.View providesMainScreenContractView() {
        return mView;
    }
}