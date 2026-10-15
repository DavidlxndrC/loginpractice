package co.edu.unicauca.saberpro.state;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;

/**
 * Pendiente de revisión: la pregunta fue enviada a evaluación. Bloquea las
 * modificaciones directas del autor (edit queda rechazado por defecto) y
 * solo permite que un revisor tome la pregunta.
 */
public class PendingReviewState implements IQuestionState {

    @Override
    public StatusQuestion getStatus() {
        return StatusQuestion.PENDING_REVIEW;
    }

    @Override
    public void startReview(Question question) {
        question.setState(new ReviewState());
    }
}
