package com.mfinance.everjoy.app.pojo;

public class PasswordControlBuilder {
    private int age;
    private String symbolList;
    private boolean allowReuse;
    private int minLength;
    private int maxLength;
    private int minLowercaseLetter;
    private int minUppercaseLetter;
    private int minDigitLetter;
    private int minSymbolLetter;
    private int maxPasswordAge;
    private boolean firstLogin;

    public PasswordControlBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public PasswordControlBuilder setSymbolList(String symbolList) {
        this.symbolList = symbolList;
        return this;
    }

    public PasswordControlBuilder setAllowReuse(boolean allowReuse) {
        this.allowReuse = allowReuse;
        return this;
    }

    public PasswordControlBuilder setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public PasswordControlBuilder setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public PasswordControlBuilder setMinLowercaseLetter(int minLowercaseLetter) {
        this.minLowercaseLetter = minLowercaseLetter;
        return this;
    }

    public PasswordControlBuilder setMinUppercaseLetter(int minUppercaseLetter) {
        this.minUppercaseLetter = minUppercaseLetter;
        return this;
    }

    public PasswordControlBuilder setMinDigitLetter(int minDigitLetter) {
        this.minDigitLetter = minDigitLetter;
        return this;
    }

    public PasswordControlBuilder setMinSymbolLetter(int minSymbolLetter) {
        this.minSymbolLetter = minSymbolLetter;
        return this;
    }

    public PasswordControlBuilder setMaxPasswordAge(int maxPasswordAge) {
        this.maxPasswordAge = maxPasswordAge;
        return this;
    }

    public PasswordControlBuilder setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
        return this;
    }

    public PasswordControl createPasswordControl() {
        return new PasswordControl(age, symbolList, allowReuse, minLength, maxLength, minLowercaseLetter, minUppercaseLetter, minDigitLetter, minSymbolLetter, maxPasswordAge, firstLogin);
    }
}