package com.loginpractice.microkernel.pipeline;

import java.util.List;

/**
 Excepción no verificada lanzada por un filtro del pipeline (o por un
 plugin) cuando una QuestionRequest no cumple las reglas de validación
 */
public class QuestionValidationException extends RuntimeException {

    private final List<String> errors;

    public QuestionValidationException(List<String> errors) {
        super(String.join("; ", errors));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
