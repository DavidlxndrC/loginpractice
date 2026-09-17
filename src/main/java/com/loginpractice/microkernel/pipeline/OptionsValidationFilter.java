package com.loginpractice.microkernel.pipeline;

import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 Verifica la cantidad y calidad de las opciones: mínimo 2 opciones,
 sin opciones vacías y sin opciones duplicadas.
 */
public class OptionsValidationFilter implements Filter<QuestionRequest> {

    private static final int MIN_OPTIONS = 2;

    @Override
    public QuestionRequest process(QuestionRequest input) {

        System.out.println("Filtro: Validando opciones...");

        List<String> errors = new ArrayList<>();
        List<String> options = input.getOptions();

        if (options == null || options.size() < MIN_OPTIONS) {
            errors.add("Debe haber al menos " + MIN_OPTIONS + " opciones.");
        } else {

            if (options.stream().anyMatch(option -> option == null || option.isBlank())) {
                errors.add("Las opciones no pueden estar vacías.");
            }

            long distinctCount = options.stream()
                    .map(String::trim)
                    .distinct()
                    .count();

            if (distinctCount != options.size()) {
                errors.add("No puede haber opciones duplicadas.");
            }
        }

        if (!errors.isEmpty()) {
            throw new QuestionValidationException(errors);
        }

        return input;
    }
}
