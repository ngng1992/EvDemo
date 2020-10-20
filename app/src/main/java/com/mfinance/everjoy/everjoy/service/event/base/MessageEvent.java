package com.mfinance.everjoy.everjoy.service.event.base;

public class MessageEvent {

    private int function;
    private LoginEvent loginFunction;

    public MessageEvent(int function, LoginEvent loginFunction) {
        this.function = function;
        this.loginFunction = loginFunction;
    }

    public int getFunction() {
        return function;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public LoginEvent getLoginFunction() {
        return loginFunction;
    }

    public void setLoginFunction(LoginEvent loginFunction) {
        this.loginFunction = loginFunction;
    }


}
