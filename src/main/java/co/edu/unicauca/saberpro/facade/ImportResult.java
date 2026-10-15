package co.edu.unicauca.saberpro.facade;

import java.util.List;

/**
 * Resumen de una importación de preguntas externas.
 */
public record ImportResult(int imported, int duplicates, List<String> errors) {
}
