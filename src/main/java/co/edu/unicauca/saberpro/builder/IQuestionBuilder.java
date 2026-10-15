package co.edu.unicauca.saberpro.builder;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.QuestionType;

/**
 * Pasos para ensamblar una pregunta (patrón Builder): contexto, enunciado,
 * opciones, justificación y clasificación. Todos los pasos devuelven el
 * propio builder para poder encadenarlos de forma fluida.
 */
public interface IQuestionBuilder {

    IQuestionBuilder reset();

    IQuestionBuilder setName(String name);

    IQuestionBuilder setType(QuestionType type);

    IQuestionBuilder setContext(String contextText);

    IQuestionBuilder setStatement(String statement);

    /** Agrega una opción; la etiqueta (A, B, C, D) se asigna según el orden. */
    IQuestionBuilder addOption(String text, boolean correct);

    IQuestionBuilder setJustification(String justification);

    IQuestionBuilder setClassification(QuestionClassification classification);

    /**
     * Entrega la pregunta ensamblada, validando que tenga el número exacto
     * de opciones de su tipo (4 por defecto) y exactamente 1 respuesta correcta.
     *
     * @throws co.edu.unicauca.saberpro.validator.QuestionValidationException si algo no cumple
     */
    Question build();
}
