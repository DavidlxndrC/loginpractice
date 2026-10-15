package co.edu.unicauca.saberpro.decorator;

import co.edu.unicauca.saberpro.domain.model.Question;

/**
 * Decorador base: envuelve otro QuestionComponent y delega todo en él.
 * Los decoradores concretos sobrescriben solo lo que modifican.
 */
public abstract class QuestionDecorator implements QuestionComponent {

    protected final QuestionComponent wrapped;

    protected QuestionDecorator(QuestionComponent wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Question getQuestion() {
        return wrapped.getQuestion();
    }

    @Override
    public double getMaxScore() {
        return wrapped.getMaxScore();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public String getFeedback() {
        return wrapped.getFeedback();
    }
}
