package com.mfinance.everjoy.app.pojo;

public class ChangePasswordRequest {
    private final String id;
    private final boolean successful;
    private final boolean pending;

    public ChangePasswordRequest(String id, boolean successful, boolean pending) {
        this.id = id;
        this.successful = successful;
        this.pending = pending;
    }

    public String getId() {
        return id;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public boolean isPending() {
        return pending;
    }
}
