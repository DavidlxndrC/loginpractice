package co.edu.unicauca.saberpro.templatemethod;

import co.edu.unicauca.saberpro.domain.model.Question;

import co.edu.unicauca.saberpro.decorator.QuestionComponent;

import java.util.Optional;
import java.util.Set;

/**
 * Verdadero/Falso: se responde con V, F, "Verdadero" o "Falso" y el puntaje
 * es completo solo si coincide con la opción correcta.
 */
public class TrueFalseEvaluator extends QuestionEvaluator {

    private static final Set<String> VALID_ANSWERS = Set.of("V", "F", "VERDADERO", "FALSO");

    @Override
    protected Optional<String> validateFormat(Question question, String answer) {

        return VALID_ANSWERS.contains(answer.toUpperCase())
                ? Optional.empty()
                : Optional.of("Formato inválido: responda V (verdadero) o F (falso).");
    }

    @Override
    protected double calculateScore(QuestionComponent component, String answer) {

        char given = Character.toUpperCase(answer.charAt(0));
        char correct = Character.toUpperCase(component.getQuestion().getCorrectOption().getText().charAt(0));

        return given == correct ? component.getMaxScore() : 0;
    }
}
