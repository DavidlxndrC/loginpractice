package co.edu.unicauca.saberpro.decorator;

import co.edu.unicauca.saberpro.domain.model.Question;

/**
 * Componente concreto: la pregunta sin características adicionales
 * (1 punto, sin retroalimentación).
 */
public class BasicQuestion implements QuestionComponent {

    private final Question question;

    public BasicQuestion(Question question) {
        this.question = question;
    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public double getMaxScore() {
        return 1.0;
    }

    @Override
    public String getDescription() {
        return question.getName() + " (" + question.getType() + ")";
    }

    @Override
    public String getFeedback() {
        return "";
    }
}
