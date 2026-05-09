package com.giovanna.saas_users.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String id) {
        super(resource + " with id " + id + " not found");
    }
}
