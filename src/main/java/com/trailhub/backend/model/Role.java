package com.trailhub.backend.model;

public enum Role {

    USER("User"),
    ADMIN("Admin");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
