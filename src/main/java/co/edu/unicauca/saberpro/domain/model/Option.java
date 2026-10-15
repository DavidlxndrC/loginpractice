package co.edu.unicauca.saberpro.domain.model;

/**
 * Opción de respuesta de una pregunta. La etiqueta (A, B, C, D) la asigna
 * el builder según la posición.
 */
public class Option {

    private final String label;
    private final String text;
    private final boolean correct;

    public Option(String label, String text, boolean correct) {
        this.label = label;
        this.text = text;
        this.correct = correct;
    }

    public String getLabel() {
        return label;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public String toString() {
        return label + ". " + text;
    }
}
