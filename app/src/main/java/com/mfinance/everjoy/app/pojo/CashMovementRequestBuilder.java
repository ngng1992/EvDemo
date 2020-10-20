package com.mfinance.everjoy.app.pojo;

public class CashMovementRequestBuilder {
    private String amount;
    private String bankacc;
    private boolean pending;
    private boolean success;
    private boolean sendURL;
    private boolean deposit;
    private String minAmount = "";
    private String maxAmount = "";
    private String url = "";
    public CashMovementRequestBuilder() {

    }

    public CashMovementRequestBuilder(CashMovementRequest t) {
        amount = t.getAmount();
        bankacc = t.getBankacc();
        pending = t.isPending();
        success = t.isSuccess();
        sendURL = t.isSendURL();
        deposit = t.isDeposit();
        minAmount = t.getMinAmount();
        maxAmount = t.getMaxAmount();
        url = t.getUrl();
    }

    public CashMovementRequestBuilder setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public CashMovementRequestBuilder setBankacc(String bankacc) {
        this.bankacc = bankacc;
        return this;
    }

    public CashMovementRequestBuilder setPending(boolean pending) {
        this.pending = pending;
        return this;
    }

    public CashMovementRequestBuilder setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public CashMovementRequestBuilder setMinAmount(String minAmount) {
        this.minAmount = minAmount;
        return this;
    }

    public CashMovementRequestBuilder setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
        return this;
    }

    public CashMovementRequestBuilder setSendURL(boolean sendURL) {
        this.sendURL = sendURL;
        return this;
    }

    public CashMovementRequestBuilder setDeposit(boolean deposit) {
        this.deposit = deposit;
        return this;
    }

    public CashMovementRequestBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public CashMovementRequest createCashMovementRequest() {
        return new CashMovementRequest(amount, bankacc, pending, success, sendURL, deposit, minAmount, maxAmount, url);
    }
}