package co.edu.unicauca.saberpro.facade;

import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.QuestionType;

import java.util.List;

/**
 * Datos crudos de una pregunta tal como los captura la capa de presentación.
 * El Facade los convierte en una Question usando el Builder.
 *
 * correctIndex es la posición (0 = A, 1 = B, ...) de la opción correcta.
 */
public record QuestionForm(
        QuestionType type,
        String name,
        String context,
        String statement,
        List<String> options,
        int correctIndex,
        String justification,
        QuestionClassification classification) {
}
