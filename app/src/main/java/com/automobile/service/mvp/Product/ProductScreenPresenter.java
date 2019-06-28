package com.automobile.service.mvp.Product;

import com.automobile.service.model.Login.LoginResponseModel;
import com.automobile.service.model.product.ProductResponseModel;
import com.automobile.service.mvp.Login.LoginScreen;
import com.automobile.service.util.ParamsConstans;
import com.automobile.service.util.WsConstants;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Class : ProductScreenPresenter
 */


public class ProductScreenPresenter implements ProductScreen.Presenter {

    public Retrofit retrofit;
    ProductScreen.View mView;

    @Inject
    public ProductScreenPresenter(Retrofit retrofit, ProductScreen.View mView) {
        this.retrofit = retrofit;
        this.mView = mView;
    }

    @Override
    public void getProductList(String searchKeyword,String pageIndex)
    {


        retrofit.create(ProductScreenService.class).getProduct(searchKeyword,pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<ProductResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ProductResponseModel responseBase) {
                        mView.showResponse(responseBase);
                    }


                });
    }


    public interface ProductScreenService {

        @GET(WsConstants.METHOD_PRODUCT_LIST)
        Observable<ProductResponseModel> getProduct(@Query(ParamsConstans.PARAM_SEARCH) String word,
                                                    @Query(ParamsConstans.PARAM_PAGE_INDEX) String pageIndex);


    }

}
