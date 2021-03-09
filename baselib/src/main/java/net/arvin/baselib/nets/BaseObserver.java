package net.arvin.baselib.nets;


import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.github.leonardoxh.livedatacalladapter.Resource;

import net.arvin.baselib.utils.ALog;


 /**
  * Created by yjl on 2021/3/8 9:56
  * Function：
  * Desc：
  */
public abstract class BaseObserver<R> implements Observer<Resource<R>> {

    @Override
    public void onChanged(@Nullable Resource<R> response) {
        if (response != null) {
            R data = response.getResource();
            ALog.json("NetRequest", data);
            if (data != null) {
                callback(data);
            } else {
                if (response.getError() != null) {
                    onError(response.getError());
                }
            }
        }
    }

    public void onError(Throwable throwable) {
    }

    public abstract void callback(R response);
}
