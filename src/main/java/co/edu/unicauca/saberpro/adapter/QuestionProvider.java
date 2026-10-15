package co.edu.unicauca.saberpro.adapter;

import co.edu.unicauca.saberpro.domain.model.Question;

import java.util.List;

/**
 * Interfaz que el Banco de Preguntas Saber PRO acepta como fuente de
 * preguntas (la interfaz "Target" del patrón Adapter).
 */
public interface QuestionProvider {

    /** Preguntas ya convertidas al modelo del banco. */
    List<Question> getQuestions();

    /** Entradas de la fuente que no pudieron convertirse en la última lectura. */
    default List<String> getErrors() {
        return List.of();
    }
}
