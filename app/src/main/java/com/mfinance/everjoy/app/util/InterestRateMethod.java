package com.mfinance.everjoy.app.util;

public enum InterestRateMethod {
    PERCENTAGE(2), FIXED(3);
    private final int value;
    InterestRateMethod(int value) {
        this.value = value;
    }
    public static InterestRateMethod fromValue(int value) {
        for (InterestRateMethod i : values()) {
            if (value == i.value) {
                return i;
            }
        }
        return FIXED;
    }
}
