package com.mfinance.everjoy.everjoy.bean.base;

/**
 * 不写泛型T，data返回的值可能是null，gson解析户出错
 */
public class BaseBean {

    /**
     * 以下是成功时，返回的信息
     * 设置默认值，0成功
     *
     * Status: 0 - OK, -1 - Internal Error, -2 - OTP not requested, -3 - OTP incorrect
     */
    private int code;
    private String message;

    // 接收其余数据
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
