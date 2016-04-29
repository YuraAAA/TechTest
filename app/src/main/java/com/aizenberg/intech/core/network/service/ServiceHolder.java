package com.aizenberg.intech.core.network.service;

import com.aizenberg.intech.core.network.IService;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Yuriy Aizenberg
 */
public class ServiceHolder {

    private static final String URL = "https://api-content-beeline.intech-global.com/public/marketplaces/1/tags/4/";
    private static final int TIMEOUT = 10;
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;

    private static IService service;

    public static synchronized IService getService() {
        if (service == null) {
            service = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(JacksonConverterFactory.create(BaseRestService.getObjectMapper()))
                    .validateEagerly(true)
                    .client(new OkHttpClient.Builder()
                            //.addInterceptor(new GzipRequestInterceptor())
                            .addInterceptor(getInterceptor())
                            .readTimeout(TIMEOUT, TIMEOUT_UNIT)
                            .connectTimeout(TIMEOUT, TIMEOUT_UNIT)
                            .writeTimeout(TIMEOUT, TIMEOUT_UNIT)
                            .connectionPool(new ConnectionPool())
                            .build())
                    .build()
                    .create(IService.class);
        }
        return service;
    }

    private static Interceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }


}
