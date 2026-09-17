package com.loginpractice.microkernel.pipeline;

import com.loginpractice.microkernel.common.entities.QuestionClassification;
import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 Valida la clasificación de la pregunta: determina la competencia, asigna
 un nivel de dificultad y establece una categoría
 */
public class ClassificationFilter implements Filter<QuestionRequest> {

    @Override
    public QuestionRequest process(QuestionRequest input) {

        System.out.println("Filtro: Clasificando competencia...");

        List<String> errors = new ArrayList<>();
        QuestionClassification classification = input.getClassification();

        if (classification == null) {
            errors.add("Debe indicar la clasificación de la pregunta (competencia, nivel de dificultad y categoría).");
        } else {

            if (isBlank(classification.getCompetency())) {
                errors.add("Debe indicar la competencia que evalúa la pregunta.");
            }
            if (isBlank(classification.getDifficultyLevel())) {
                errors.add("Debe indicar el nivel de dificultad de la pregunta.");
            }
            if (isBlank(classification.getCategory())) {
                errors.add("Debe indicar la categoría de la pregunta.");
            }
        }

        if (!errors.isEmpty()) {
            throw new QuestionValidationException(errors);
        }

        return input;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
