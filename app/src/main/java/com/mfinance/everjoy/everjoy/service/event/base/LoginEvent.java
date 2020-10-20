package com.mfinance.everjoy.everjoy.service.event.base;

public class LoginEvent implements BaseEvent {

    private String username;
    private String password;

    public LoginEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginEvent{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
