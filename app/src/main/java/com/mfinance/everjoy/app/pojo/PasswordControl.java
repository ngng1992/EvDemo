package com.mfinance.everjoy.app.pojo;

public final class PasswordControl {
    private final int age;
    private final String symbolList;
    private final boolean allowReuse;
    private final int minLength;
    private final int maxLength;
    private final int minLowercaseLetter;
    private final int minUppercaseLetter;
    private final int minDigitLetter;
    private final int minSymbolLetter;
    private final int maxPasswordAge;
    private final boolean firstLogin;

    PasswordControl(int age, String symbolList, boolean allowReuse, int minLength, int maxLength, int minLowercaseLetter, int minUppercaseLetter, int minDigitLetter, int minSymbolLetter, int maxPasswordAge, boolean firstLogin) {
        this.age = age;
        this.symbolList = symbolList;
        this.allowReuse = allowReuse;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.minLowercaseLetter = minLowercaseLetter;
        this.minUppercaseLetter = minUppercaseLetter;
        this.minDigitLetter = minDigitLetter;
        this.minSymbolLetter = minSymbolLetter;
        this.maxPasswordAge = maxPasswordAge;
        this.firstLogin = firstLogin;
    }

    public int getAge() {
        return age;
    }

    public String getSymbolList() {
        return symbolList;
    }

    public boolean isAllowReuse() {
        return allowReuse;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMinLowercaseLetter() {
        return minLowercaseLetter;
    }

    public int getMinUppercaseLetter() {
        return minUppercaseLetter;
    }

    public int getMinDigitLetter() {
        return minDigitLetter;
    }

    public int getMinSymbolLetter() {
        return minSymbolLetter;
    }

    public int getMaxPasswordAge() {
        return maxPasswordAge;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }
}
