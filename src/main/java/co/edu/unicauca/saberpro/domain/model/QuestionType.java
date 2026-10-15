package co.edu.unicauca.saberpro.domain.model;

/**
 * Tipos de pregunta que maneja el Banco de Preguntas Saber PRO.
 * Cada tipo conoce cuántas opciones debe tener (el builder y el validador
 * usan este dato, por lo que no hay condicionales por tipo).
 */
public enum QuestionType {

    MULTIPLE_CHOICE("Selección múltiple", 4),
    TRUE_FALSE("Verdadero/Falso", 2),
    CASE_STUDY("Caso de estudio", 4);

    private final String label;
    private final int optionCount;

    QuestionType(String label, int optionCount) {
        this.label = label;
        this.optionCount = optionCount;
    }

    public int getOptionCount() {
        return optionCount;
    }

    @Override
    public String toString() {
        return label;
    }
}
