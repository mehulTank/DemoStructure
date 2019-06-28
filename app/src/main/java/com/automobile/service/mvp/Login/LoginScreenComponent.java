package com.automobile.service.mvp.Login;


import com.automobile.service.Activity.LoginActivity;
import com.automobile.service.data.component.NetComponent;
import com.automobile.service.util.CustomScope;

import dagger.Component;

/**
 * Created by
 */
@CustomScope
@Component(dependencies = NetComponent.class,
        modules = LoginScreenModule.class)
public interface LoginScreenComponent {
    void inject(LoginActivity activity);
}
