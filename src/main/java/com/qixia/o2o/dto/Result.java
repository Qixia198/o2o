package com.qixia.o2o.dto;

/**
 * @author 七夏
 * @create 2020-04-17
 */
public class Result<T> {
    //是否成功
    private boolean success;
    //成功后返回的数据
    private T data;
    //错误信息
    private String errMsg;
    //错误状态码
    private int errorCode;

    public Result() {
    }

    //成功时的构造器
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    //错误时的构造器

    public Result(boolean success, String errMsg, int errorCode) {
        this.success = success;
        this.errMsg = errMsg;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
