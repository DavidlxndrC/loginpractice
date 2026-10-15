package co.edu.unicauca.saberpro.state;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;

/**
 * En revisión: revisión por pares. El revisor puede aprobar la pregunta o
 * solicitar correcciones (la pregunta vuelve a Borrador).
 */
public class ReviewState implements IQuestionState {

    @Override
    public StatusQuestion getStatus() {
        return StatusQuestion.IN_REVIEW;
    }

    @Override
    public void approve(Question question) {
        question.setState(new ApprovedState());
    }

    @Override
    public void requestCorrections(Question question) {
        question.setState(new DraftState());
    }
}
