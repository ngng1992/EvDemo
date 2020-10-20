package com.mfinance.everjoy.app.pojo;

public class CancelCastMovementRequest {
    private final String requestID;
    private final boolean successful;
    private final boolean pending;

    public CancelCastMovementRequest(String requestID, boolean successful, boolean pending) {
        this.requestID = requestID;
        this.successful = successful;
        this.pending = pending;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getRequestID() {
        return requestID;
    }

    public boolean isPending() {
        return pending;
    }
}
