package com.asss.management.controller;

public class MyCustomResponse {
    private String message;

    public MyCustomResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}