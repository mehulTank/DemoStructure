package com.automobile.service.mvp.ForgotPsw;


import com.automobile.service.Activity.ForgotActivity;
import com.automobile.service.Activity.RegisterActivity;
import com.automobile.service.data.component.NetComponent;
import com.automobile.service.mvp.Register.RegisterScreenModule;
import com.automobile.service.util.CustomScope;

import dagger.Component;

/**
 * Created by
 */
@CustomScope
@Component(dependencies = NetComponent.class,
        modules = ForgotScreenModule.class)
public interface ForgotScreenComponent {
    void inject(ForgotActivity activity);
}
