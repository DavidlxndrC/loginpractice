package co.edu.unicauca.saberpro.domain.model;

/**
 * Estados del ciclo de vida de una pregunta. Es solo la "etiqueta" visible
 * (la usan las vistas del Observer); el comportamiento de cada estado vive
 * en el paquete state (patrón State).
 */
public enum StatusQuestion {

    DRAFT("Borrador"),
    PENDING_REVIEW("Pendiente de revisión"),
    IN_REVIEW("En revisión"),
    APPROVED("Aprobada"),
    PUBLISHED("Publicada");

    private final String label;

    StatusQuestion(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
