package com.loginpractice.domain;

public enum StatusQuestion {

    DRAFT("Borrador"),
    PENDING_REVIEW("Pendiente de revisión"),
    DELETED("Eliminada");

    private final String label;

    StatusQuestion(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
