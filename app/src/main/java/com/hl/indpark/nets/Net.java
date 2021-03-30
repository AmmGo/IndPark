package com.hl.indpark.nets;

import net.arvin.baselib.nets.BaseNet;

import okhttp3.OkHttpClient;


 /**
  * Created by yjl on 2021/3/30 15:36
  * Function：
  * Desc：
  */
public class Net extends BaseNet<Api> {

    private static Net INSTANCE = new Net();

    private Net() {
    }

    @Override
    protected void addHttpConfig(OkHttpClient.Builder clientBuilder) {
        super.addHttpConfig(clientBuilder);
        clientBuilder.addInterceptor(new CookieInterceptor());
    }

    @Override
    protected String getDefaultBaseUrl() {
        return Api.BASE_URL;
    }

    public static Net get() {
        return INSTANCE;
    }

    public static Api api() {
        return get().getApi();
    }
}
