package co.edu.unicauca.saberpro.templatemethod;

import co.edu.unicauca.saberpro.decorator.QuestionComponent;
import co.edu.unicauca.saberpro.domain.model.Question;

import java.util.Optional;

/**
 * Plantilla del algoritmo de evaluación (patrón Template Method):
 *
 *   1) recibir la respuesta  2) validar el formato
 *   3) calcular el puntaje (específico de cada tipo)  4) generar la retroalimentación
 *
 * evaluate() es final: el orden de los pasos no cambia. Las subclases solo
 * aportan lo que depende del tipo de pregunta (calculateScore y, si lo
 * necesitan, validateFormat).
 */
public abstract class QuestionEvaluator {

    public final EvaluationResult evaluate(QuestionComponent component, String rawAnswer) {

        String answer = receiveAnswer(rawAnswer);

        Optional<String> formatError = validateFormat(component.getQuestion(), answer);
        if (formatError.isPresent()) {
            return EvaluationResult.invalidFormat(component.getMaxScore(), formatError.get());
        }

        double score = calculateScore(component, answer);

        String feedback = generateFeedback(component, answer, score);

        return new EvaluationResult(true, score, component.getMaxScore(), feedback);
    }

    // Paso 1: recibir la respuesta
    protected String receiveAnswer(String rawAnswer) {
        return rawAnswer == null ? "" : rawAnswer.trim();
    }

    // Paso 2: validar el formato. Por defecto se espera la letra de una opción (A, B, C, D).
    // Devuelve el mensaje de error, o vacío si el formato es correcto.
    protected Optional<String> validateFormat(Question question, String answer) {

        boolean isOptionLabel = question.getOptions().stream()
                .anyMatch(option -> option.getLabel().equalsIgnoreCase(answer));

        return isOptionLabel
                ? Optional.empty()
                : Optional.of("Formato inválido: responda con la letra de una opción (" + optionLabels(question) + ").");
    }

    // Paso 3: calcular el puntaje (depende del tipo de pregunta)
    protected abstract double calculateScore(QuestionComponent component, String answer);

    // Paso 4: generar la retroalimentación (se completa con la de los decoradores)
    protected String generateFeedback(QuestionComponent component, String answer, double score) {
        return (verdict(component, score) + " " + component.getFeedback()).trim();
    }

    private String verdict(QuestionComponent component, double score) {

        if (score >= component.getMaxScore()) {
            return "¡Respuesta correcta!";
        }
        if (score > 0) {
            return "Respuesta parcialmente correcta.";
        }
        return "Respuesta incorrecta. La respuesta correcta es: " + component.getQuestion().getCorrectOption() + ".";
    }

    protected String optionLabels(Question question) {
        return String.join(", ", question.getOptions().stream().map(option -> option.getLabel()).toList());
    }
}
