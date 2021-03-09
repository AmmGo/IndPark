package com.hl.indpark.nets;

import net.arvin.baselib.nets.BaseObserver;
import net.arvin.baselib.utils.ALog;
import com.hl.indpark.entities.Response;

/**
 * Created by arvinljw on 2018/10/31 17:13
 * Function：
 * Desc：
 */
public abstract class ApiObserver<R> extends BaseObserver<Response<R>> {
    private static final String TAG = "Request";

    @Override
    public void callback(Response<R> response) {
        if (response.isSuccess()) {
            onSuccess(response);
        } else {
            onFailure(response.getErrorCode(), response.getErrorMsg());
        }
    }

    public void onFailure(int code, String msg) {
        ALog.d(TAG, "code:" + code + "->msg:" + msg);
    }

    public abstract void onSuccess(Response<R> response);
}
