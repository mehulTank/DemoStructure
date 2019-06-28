package com.automobile.service.data.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.automobile.service.util.WsConstants;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by
 */
@Module
public class NetModule {

    String mBaseUrl;
    private final int TIME = 60;

    public NetModule(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {
        /*OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);*/
        HttpLoggingInterceptor logging = new
                HttpLoggingInterceptor();
        logging.
                setLevel(HttpLoggingInterceptor.Level.BODY);

                /*OkHttpClient okClient =
                        new OkHttpClient.Builder().addInterceptor(logging).build();*/

        OkHttpClient okClient =
                new OkHttpClient.Builder()
                        .connectTimeout(TIME, TimeUnit.SECONDS)
                        .writeTimeout(TIME, TimeUnit.SECONDS)
                        .readTimeout(TIME, TimeUnit.SECONDS)
                        .addInterceptor(logging)
                        .build();
        return okClient;

    }


    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(WsConstants.BASE_URL)
                .build();



        return retrofit;
    }


}
