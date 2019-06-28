package com.automobile.service.mvp.ChangePsw;

import com.automobile.service.mvp.ForgotPsw.ForgotScreen;
import com.automobile.service.util.CustomScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangePswScreenModule
{
    private final ChangePswScreen.View mView;


    public ChangePswScreenModule(ChangePswScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    @CustomScope
    ChangePswScreen.View providesMainScreenContractView() {
        return mView;
    }
}