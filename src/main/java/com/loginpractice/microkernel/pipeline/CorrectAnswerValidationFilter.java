package com.loginpractice.microkernel.pipeline;

import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 Verifica que exista una respuesta correcta, que pertenezca a las
 opciones registradas y que los datos sean consistentes.
 */
public class CorrectAnswerValidationFilter implements Filter<QuestionRequest> {

    @Override
    public QuestionRequest process(QuestionRequest input) {

        System.out.println("Filtro: Validando respuesta correcta...");

        List<String> errors = new ArrayList<>();
        String correctAnswer = input.getCorrectAnswer();
        List<String> options = input.getOptions();

        if (correctAnswer == null || correctAnswer.isBlank()) {
            errors.add("Debe existir una respuesta correcta.");
        } else if (options != null && !options.isEmpty()
                && options.stream().noneMatch(option -> option.trim().equalsIgnoreCase(correctAnswer.trim()))) {
            errors.add("La respuesta correcta debe pertenecer a las opciones registradas.");
        }

        if (!errors.isEmpty()) {
            throw new QuestionValidationException(errors);
        }

        return input;
    }
}
