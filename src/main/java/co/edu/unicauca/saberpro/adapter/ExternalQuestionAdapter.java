package co.edu.unicauca.saberpro.adapter;

import co.edu.unicauca.saberpro.builder.IQuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.QuestionType;
import co.edu.unicauca.saberpro.validator.QuestionValidationException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapter: hace que ExternalQuestionService (JSON con campos propios) cumpla
 * la interfaz QuestionProvider que espera el banco. Cada entrada externa se
 * traduce con el QuestionBuilder, de modo que también pasa por la validación
 * (exactamente 4 opciones y 1 respuesta correcta). Las entradas inválidas no
 * detienen la importación: se omiten y se reportan en getErrors().
 */
public class ExternalQuestionAdapter implements QuestionProvider {

    private static final Map<String, QuestionType> EXTERNAL_TYPES = Map.of(
            "SELECCION_MULTIPLE", QuestionType.MULTIPLE_CHOICE,
            "VERDADERO_FALSO", QuestionType.TRUE_FALSE,
            "CASO_ESTUDIO", QuestionType.CASE_STUDY
    );

    private final ExternalQuestionService externalService;
    private final List<String> errors = new ArrayList<>();

    public ExternalQuestionAdapter(ExternalQuestionService externalService) {
        this.externalService = externalService;
    }

    @Override
    public List<Question> getQuestions() {

        errors.clear();
        List<Question> questions = new ArrayList<>();

        JsonArray entries;
        try {
            entries = JsonParser.parseString(externalService.fetchQuestionsJson()).getAsJsonArray();
        } catch (JsonParseException | IllegalStateException ex) {
            errors.add("El servicio externo devolvió un JSON ilegible: " + ex.getMessage());
            return questions;
        }

        for (JsonElement element : entries) {
            JsonObject entry = element.getAsJsonObject();
            try {
                questions.add(convert(entry));
            } catch (QuestionValidationException ex) {
                errors.add(text(entry, "codigo") + ": " + String.join(" ", ex.getErrors()));
            } catch (RuntimeException ex) {
                errors.add(text(entry, "codigo") + ": entrada con formato inesperado (" + ex.getMessage() + ").");
            }
        }

        return questions;
    }

    @Override
    public List<String> getErrors() {
        return List.copyOf(errors);
    }

    private Question convert(JsonObject entry) {

        QuestionType type = EXTERNAL_TYPES.get(text(entry, "tipo"));
        if (type == null) {
            throw new IllegalArgumentException("tipo desconocido \"" + text(entry, "tipo") + "\"");
        }

        int correctIndex = text(entry, "clave").toUpperCase().charAt(0) - 'A';

        IQuestionBuilder builder = new QuestionBuilder()
                .setType(type)
                .setName(text(entry, "titulo"))
                .setContext(text(entry, "contexto"))
                .setStatement(text(entry, "enunciado"));

        JsonArray alternatives = entry.getAsJsonArray("alternativas");
        for (int i = 0; i < alternatives.size(); i++) {
            builder.addOption(alternatives.get(i).getAsString(), i == correctIndex);
        }

        return builder
                .setJustification(text(entry, "explicacion"))
                .setClassification(new QuestionClassification(
                        text(entry, "competencia"), text(entry, "nivel"), text(entry, "categoria")))
                .build();
    }

    private String text(JsonObject entry, String field) {
        JsonElement value = entry.get(field);
        return value == null || value.isJsonNull() ? "" : value.getAsString();
    }
}
