package com.mfinance.everjoy.everjoy.bean.base;

/**
 * 不写泛型T，data返回的值可能是null，gson解析户出错
 */
public class BaseBean {

    /**
     * 以下是成功时，返回的信息
     * 设置默认值，0成功
     */
    private int code;
    private String message;

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
