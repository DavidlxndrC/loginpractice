package co.edu.unicauca.saberpro.state;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;

/**
 * Borrador: estado inicial. Permite la edición completa del enunciado y las
 * opciones, y transiciona a "Pendiente de revisión".
 */
public class DraftState implements IQuestionState {

    @Override
    public StatusQuestion getStatus() {
        return StatusQuestion.DRAFT;
    }

    @Override
    public void edit(Question question, Question newContent) {
        question.copyContentFrom(newContent);
    }

    @Override
    public void submitForReview(Question question) {
        question.setState(new PendingReviewState());
    }
}
