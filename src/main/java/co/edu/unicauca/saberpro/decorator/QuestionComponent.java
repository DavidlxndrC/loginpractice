package co.edu.unicauca.saberpro.decorator;

import co.edu.unicauca.saberpro.domain.model.Question;

/**
 * Componente del patrón Decorator: lo que se puede pedir a una pregunta
 * "lista para evaluar" (puntaje máximo, descripción y retroalimentación),
 * ya sea la pregunta básica o una envuelta por uno o varios decoradores.
 */
public interface QuestionComponent {

    Question getQuestion();

    /** Puntaje máximo que se obtiene al responder correctamente. */
    double getMaxScore();

    String getDescription();

    /** Explicación técnica que se adjunta al resultado (vacía si no hay). */
    String getFeedback();
}
