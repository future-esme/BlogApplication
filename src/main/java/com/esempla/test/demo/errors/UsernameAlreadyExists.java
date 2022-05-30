package com.esempla.test.demo.errors;

public class UsernameAlreadyExists extends BadRequestException {
    private static final long serialVersionUID = 1L;

    public UsernameAlreadyExists() {
        super("Username already used!", "userManagement", "userexists");
    }
}
