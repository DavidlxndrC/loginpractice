package com.loginpractice.microkernel.pipeline;

import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 Valida que la pregunta tenga texto válido y cumpla reglas básicas:
 texto no vacío, longitud mínima y formato correcto.
 */
public class ContentValidationFilter implements Filter<QuestionRequest> {

    private static final int MIN_LENGTH = 10;

    @Override
    public QuestionRequest process(QuestionRequest input) {

        System.out.println("Filtro: Validando contenido...");

        List<String> errors = new ArrayList<>();
        String content = input.getContent();

        if (content == null || content.isBlank()) {
            errors.add("El texto de la pregunta no puede estar vacío.");
        } else if (content.trim().length() < MIN_LENGTH) {
            errors.add("El texto de la pregunta debe tener al menos " + MIN_LENGTH + " caracteres.");
        }

        if (!errors.isEmpty()) {
            throw new QuestionValidationException(errors);
        }

        return input;
    }
}
