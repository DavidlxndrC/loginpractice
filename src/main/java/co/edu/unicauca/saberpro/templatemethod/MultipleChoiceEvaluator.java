package co.edu.unicauca.saberpro.templatemethod;

import co.edu.unicauca.saberpro.decorator.QuestionComponent;

/**
 * Selección múltiple: puntaje completo si la letra elegida es la opción correcta; si no, cero.
 */
public class MultipleChoiceEvaluator extends QuestionEvaluator {

    @Override
    protected double calculateScore(QuestionComponent component, String answer) {

        String correctLabel = component.getQuestion().getCorrectOption().getLabel();

        return correctLabel.equalsIgnoreCase(answer) ? component.getMaxScore() : 0;
    }
}
