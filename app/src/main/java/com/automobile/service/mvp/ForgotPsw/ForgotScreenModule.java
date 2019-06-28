package com.automobile.service.mvp.ForgotPsw;

import com.automobile.service.mvp.Register.RegisterScreen;
import com.automobile.service.util.CustomScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ForgotScreenModule
{
    private final ForgotScreen.View mView;


    public ForgotScreenModule(ForgotScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    @CustomScope
    ForgotScreen.View providesMainScreenContractView() {
        return mView;
    }
}