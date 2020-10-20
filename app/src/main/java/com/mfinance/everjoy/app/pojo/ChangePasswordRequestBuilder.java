package com.mfinance.everjoy.app.pojo;

public class ChangePasswordRequestBuilder {
    private boolean successful;
    private boolean pending;
    private String id;

    public ChangePasswordRequestBuilder() {

    }

    public ChangePasswordRequestBuilder(ChangePasswordRequest request) {
        this.id = request.getId();
        this.successful = request.isSuccessful();
        this.pending = request.isPending();
    }

    public ChangePasswordRequestBuilder setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public ChangePasswordRequestBuilder setPending(boolean pending) {
        this.pending = pending;
        return this;
    }

    public ChangePasswordRequest createChangePasswordRequest() {
        return new ChangePasswordRequest(id, successful, pending);
    }

    public ChangePasswordRequestBuilder setId(String id) {
        this.id = id;
        return this;
    }
}