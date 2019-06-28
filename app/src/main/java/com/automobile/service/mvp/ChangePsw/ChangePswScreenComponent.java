package com.automobile.service.mvp.ChangePsw;


import com.automobile.service.Activity.ForgotActivity;
import com.automobile.service.data.component.NetComponent;
import com.automobile.service.fragment.ChangePswFragment;
import com.automobile.service.mvp.ForgotPsw.ForgotScreenModule;
import com.automobile.service.util.CustomScope;

import dagger.Component;

/**
 * Created by
 */
@CustomScope
@Component(dependencies = NetComponent.class,
        modules = ChangePswScreenModule.class)
public interface ChangePswScreenComponent {
    void inject(ChangePswFragment changePswFragment);
}
