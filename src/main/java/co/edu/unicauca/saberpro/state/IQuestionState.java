package co.edu.unicauca.saberpro.state;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;

/**
 * Estado del ciclo de vida de una pregunta (patrón State).
 *
 * Todas las operaciones están rechazadas por defecto; cada estado concreto
 * sobrescribe únicamente las que le están permitidas. Así las transiciones
 * inválidas se rechazan sin ningún if/else ni switch.
 *
 * Ciclo de vida:
 *   Borrador -> Pendiente de revisión -> En revisión -> Aprobada -> Publicada
 *   (y En revisión -> Borrador cuando el revisor solicita correcciones)
 */
public interface IQuestionState {

    StatusQuestion getStatus();

    default void edit(Question question, Question newContent) {
        throw rejected("editar");
    }

    default void submitForReview(Question question) {
        throw rejected("enviar a revisión");
    }

    default void startReview(Question question) {
        throw rejected("iniciar la revisión de");
    }

    default void approve(Question question) {
        throw rejected("aprobar");
    }

    default void requestCorrections(Question question) {
        throw rejected("solicitar correcciones a");
    }

    default void publish(Question question) {
        throw rejected("publicar");
    }

    private IllegalStateException rejected(String action) {
        return new IllegalStateException(
                "No se puede " + action + " una pregunta en estado \"" + getStatus() + "\".");
    }
}
