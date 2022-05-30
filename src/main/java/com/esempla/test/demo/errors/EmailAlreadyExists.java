package com.esempla.test.demo.errors;

public class EmailAlreadyExists extends BadRequestException{
    private static final long serialVersionUID = 1L;

    public EmailAlreadyExists() {
        super("Email already in use!", "userManagement", "emailexists");
    }
}
