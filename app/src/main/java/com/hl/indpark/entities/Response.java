package com.hl.indpark.entities;

 /**
  * Created by yjl on 2021/3/8 17:33
  * Function：
  * Desc：
  */
public class Response<T> {
    private int code;
    private String msg;
    private T data;

    public int getErrorCode() {
        return code;
    }

    public void setErrorCode(int errorCode) {
        this.code = errorCode;
    }

    public String getErrorMsg() {
        return msg;
    }

    public void setErrorMsg(String errorMsg) {
        this.msg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code == 200;
    }
}
