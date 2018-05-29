package com.happok.live.streaminfo.controller.result;


public enum  RestServiceError {
    //RPC层调用错误码
    REST_OK(0,"服务正常"),
    REST_ERROR(1,"内部处理异常"),
    REST_NOT_EXISTED(2,"不存在"),
    REST_PARAM_ERROR(3,"参数错误"),
    REST_EXISTED(4,"已存在");

    private String msg;
    private int code;

    private RestServiceError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }
}

