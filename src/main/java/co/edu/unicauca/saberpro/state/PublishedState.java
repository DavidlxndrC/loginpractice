package co.edu.unicauca.saberpro.state;

import co.edu.unicauca.saberpro.domain.model.StatusQuestion;

/**
 * Publicada: la pregunta ya está disponible en simulacros. Es un estado
 * final, por eso no habilita ninguna operación.
 */
public class PublishedState implements IQuestionState {

    @Override
    public StatusQuestion getStatus() {
        return StatusQuestion.PUBLISHED;
    }
}
