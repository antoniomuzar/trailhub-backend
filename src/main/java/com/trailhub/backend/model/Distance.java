package com.trailhub.backend.model;

public enum Distance {

    FIVE_K("5K"),
    TEN_K("10K"),
    HALF_MARATHON("HalfMarathon"),
    MARATHON("Marathon");

    private final String label;

    Distance(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
