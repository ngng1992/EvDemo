package com.mfinance.everjoy.app.pojo;

public class CancelCastMovementRequestBuilder {
    private String requestID;
    private boolean successful;
    private boolean pending;

    public CancelCastMovementRequestBuilder() {

    }

    public CancelCastMovementRequestBuilder(CancelCastMovementRequest r) {
        this.requestID = r.getRequestID();
        this.successful = r.isSuccessful();
        this.pending = r.isPending();
    }
    public CancelCastMovementRequestBuilder setRequestID(String requestID) {
        this.requestID = requestID;
        return this;
    }

    public CancelCastMovementRequestBuilder setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public CancelCastMovementRequestBuilder setPending(boolean pending) {
        this.pending = pending;
        return this;
    }

    public CancelCastMovementRequest createCancelCastMovementRequest() {
        return new CancelCastMovementRequest(requestID, successful, pending);
    }
}