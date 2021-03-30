package com.hl.indpark.nets;

import com.hl.indpark.entities.Response;

import net.arvin.baselib.nets.BaseObserver;
import net.arvin.baselib.utils.ALog;


 /**
  * Created by yjl on 2021/3/30 15:35
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
