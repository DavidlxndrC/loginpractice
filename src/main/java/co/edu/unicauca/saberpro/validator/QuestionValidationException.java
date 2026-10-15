package co.edu.unicauca.saberpro.validator;

import java.util.List;

/**
 * Se lanza cuando una pregunta incumple una o más reglas del banco.
 * Conserva la lista completa de errores para mostrarla al usuario.
 */
public class QuestionValidationException extends RuntimeException {

    private final List<String> errors;

    public QuestionValidationException(List<String> errors) {
        super("La pregunta no es válida: " + String.join(" | ", errors));
        this.errors = List.copyOf(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
