package com.mfinance.everjoy.app.pojo;

public class CashMovementRequest {
    private final String amount;
    private final String bankacc;
    private final boolean pending;
    private final boolean success;
    private final boolean sendURL;
    private final boolean deposit;
    private final String minAmount;
    private final String maxAmount;
    private final String url;

    public CashMovementRequest(String amount, String bankacc, boolean pending, boolean success, boolean sendURL, boolean deposit, String minAmount, String maxAmount, String url) {
        this.amount = amount;
        this.bankacc = bankacc;
        this.pending = pending;
        this.success = success;
        this.sendURL = sendURL;
        this.deposit = deposit;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.url = url;
    }

    public String getAmount() {
        return amount;
    }

    public String getBankacc() {
        return bankacc;
    }

    public boolean isPending() {
        return pending;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public boolean isSendURL() {
        return sendURL;
    }

    public boolean isDeposit() {
        return deposit;
    }

    public String getUrl() {
        return url;
    }
}
