package co.edu.unicauca.saberpro.validator;

import co.edu.unicauca.saberpro.domain.model.Option;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reglas de calidad de una pregunta. Es el "validador" que usan el Builder
 * (en build()) y el Facade (antes de enviar a revisión).
 *
 * Reglas:
 *  - nombre y enunciado no vacíos;
 *  - los casos de estudio requieren contexto;
 *  - número exacto de opciones según el tipo (4 en selección múltiple y caso
 *    de estudio, 2 en verdadero/falso);
 *  - opciones con texto y sin duplicados;
 *  - exactamente 1 respuesta correcta.
 */
public class QuestionValidator {

    public List<String> validate(Question question) {

        List<String> errors = new ArrayList<>();

        if (isBlank(question.getName())) {
            errors.add("El nombre de la pregunta no puede estar vacío.");
        }
        if (isBlank(question.getStatement())) {
            errors.add("El enunciado no puede estar vacío.");
        }
        if (question.getType() == null) {
            errors.add("Debe indicarse el tipo de pregunta.");
            return errors;
        }
        if (question.getType() == QuestionType.CASE_STUDY
                && (question.getContext() == null || question.getContext().isEmpty())) {
            errors.add("Un caso de estudio requiere un contexto.");
        }

        validateOptions(question, errors);

        return errors;
    }

    public void validateOrThrow(Question question) {

        List<String> errors = validate(question);

        if (!errors.isEmpty()) {
            throw new QuestionValidationException(errors);
        }
    }

    private void validateOptions(Question question, List<String> errors) {

        List<Option> options = question.getOptions();
        int expected = question.getType().getOptionCount();

        if (options.size() != expected) {
            errors.add("Una pregunta de tipo \"" + question.getType() + "\" debe tener exactamente "
                    + expected + " opciones (tiene " + options.size() + ").");
        }

        Set<String> seen = new HashSet<>();
        for (Option option : options) {
            if (isBlank(option.getText())) {
                errors.add("La opción " + option.getLabel() + " no puede estar vacía.");
            } else if (!seen.add(option.getText().trim().toLowerCase())) {
                errors.add("La opción " + option.getLabel() + " está duplicada.");
            }
        }

        long correct = options.stream().filter(Option::isCorrect).count();
        if (correct != 1) {
            errors.add("Debe existir exactamente 1 respuesta correcta (hay " + correct + ").");
        }
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}
