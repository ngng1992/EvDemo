package com.mfinance.everjoy.app.bo;

public enum AccountCreditType {
    MARGIN(0), CREDIT_LIMIT(1), FREE_LOT(2);
    private int value;
    AccountCreditType(int i) {
        value = i;
    }
    public static AccountCreditType fromInteger(int t) {
        for (AccountCreditType type : values()) {
            if (type.value == t) {
                return type;
            }
        }
        return MARGIN;
    }

}
