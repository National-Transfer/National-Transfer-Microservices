package com.ensa.kycservice.entities;

public enum Title {
    M("M."),
    MME("Mme.");

    private final String title;

    Title(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
