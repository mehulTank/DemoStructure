package com.automobile.service.mvp.Register;


import com.automobile.service.Activity.LoginActivity;
import com.automobile.service.Activity.RegisterActivity;
import com.automobile.service.data.component.NetComponent;
import com.automobile.service.mvp.Login.LoginScreenModule;
import com.automobile.service.util.CustomScope;

import dagger.Component;

/**
 * Created by
 */
@CustomScope
@Component(dependencies = NetComponent.class,
        modules = RegisterScreenModule.class)
public interface RegisterScreenComponent {
    void inject(RegisterActivity activity);
}
