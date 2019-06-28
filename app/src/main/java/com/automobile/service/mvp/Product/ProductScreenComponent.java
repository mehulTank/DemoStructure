package com.automobile.service.mvp.Product;

import com.automobile.service.data.component.NetComponent;
import com.automobile.service.fragment.ProductListFragment;
import com.automobile.service.util.CustomScope;

import dagger.Component;

/**
 * Created date
 */
@CustomScope
@Component(dependencies = NetComponent.class, modules = ProductScreenModule.class)
public interface ProductScreenComponent {
    void inject(ProductListFragment fragment);
}
