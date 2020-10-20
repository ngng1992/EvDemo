package com.mfinance.everjoy.app.constant;

public enum MenuServiceID {
    DEPOSIT(1),
    WITHDRAWAL(2),
    NEWS(3),
    ECONOMIC_IND(4),
    CONTRACT_US(5),
    TERMS(6),
    ANNOUNCEMENT(7);
    private final int i;
    MenuServiceID(int i) {
        this.i = i;
    }
    public int getValue() {
        return i;
    }
}
