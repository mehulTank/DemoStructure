package com.automobile.service.mvp.Product;

import com.automobile.service.model.product.ProductResponseModel;

public interface ProductScreen {
    interface View {
        void showResponse(ProductResponseModel responseBase);

        void showError(String message);


    }

    interface Presenter {
        void getProductList(String searchKeyword, String pageIndex);

    }
}
