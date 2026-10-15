package co.edu.unicauca.saberpro.state;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;

/**
 * Aprobada: pregunta validada pedagógicamente y habilitada para publicación
 * en simulacros.
 */
public class ApprovedState implements IQuestionState {

    @Override
    public StatusQuestion getStatus() {
        return StatusQuestion.APPROVED;
    }

    @Override
    public void publish(Question question) {
        question.setState(new PublishedState());
    }
}
