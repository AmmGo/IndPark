package com.hl.indpark.nets;

import androidx.annotation.NonNull;

import com.hl.indpark.utils.SharePreferenceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


 /**
  * Created by yjl on 2021/3/30 15:36
  * Function：
  * Desc：
  */
class CookieInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String token = SharePreferenceUtil.getKeyValue("token");
        if (token != null&&!token.equals("")) {
            builder.addHeader("TOKEN", token);
            builder.addHeader("terminal", "android");
//            builder.addHeader("Cookie", "loginUserPassword=" + user.getPassword());
        }
        return chain.proceed(builder.build());
    }
}
