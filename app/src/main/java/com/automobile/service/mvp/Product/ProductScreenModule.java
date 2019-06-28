package com.automobile.service.mvp.Product;


import com.automobile.service.util.CustomScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ProductScreenModule
{
    private final ProductScreen.View mView;


    public ProductScreenModule(ProductScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    @CustomScope
    ProductScreen.View providesMainScreenContractView() {
        return mView;
    }
}