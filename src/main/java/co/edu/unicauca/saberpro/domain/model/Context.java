package co.edu.unicauca.saberpro.domain.model;

/**
 * Contexto (situación, caso o texto base) sobre el cual se formula el enunciado.
 * Puede estar vacío en preguntas simples; es obligatorio en los casos de estudio.
 */
public class Context {

    private final String text;

    public Context(String text) {
        this.text = text == null ? "" : text.trim();
    }

    public String getText() {
        return text;
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }

    @Override
    public String toString() {
        return text;
    }
}
