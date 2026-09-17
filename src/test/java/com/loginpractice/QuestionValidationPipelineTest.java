package com.loginpractice;

import com.loginpractice.microkernel.pipeline.ClassificationFilter;
import com.loginpractice.microkernel.pipeline.ContentValidationFilter;
import com.loginpractice.microkernel.pipeline.CorrectAnswerValidationFilter;
import com.loginpractice.microkernel.pipeline.OptionsValidationFilter;
import com.loginpractice.microkernel.pipeline.Pipeline;
import com.loginpractice.microkernel.pipeline.QuestionValidationException;
import com.loginpractice.microkernel.common.entities.QuestionClassification;
import com.loginpractice.microkernel.common.entities.QuestionRequest;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionValidationPipelineTest {

    private QuestionClassification validClassification() {
        return new QuestionClassification("Arquitectura de software", "Media", "Patrones de diseño");
    }

    private QuestionRequest validRequest() {
        return new QuestionRequest(
                "MULTIPLE_CHOICE", "Pregunta válida",
                "¿Cuál es el propósito principal del patrón Microkernel?",
                List.of("Extensibilidad mediante plugins", "Ocultar datos", "Optimizar memoria", "Cifrar datos"),
                "Extensibilidad mediante plugins", validClassification(), null
        );
    }

    @Test
    void shouldPassAllFiltersWhenRequestIsValid() {

        Pipeline<QuestionRequest> pipeline = new Pipeline<>(List.of(
                new ContentValidationFilter(),
                new OptionsValidationFilter(),
                new ClassificationFilter(),
                new CorrectAnswerValidationFilter()
        ));

        QuestionRequest result = pipeline.execute(validRequest());

        assertEquals(validRequest().getContent(), result.getContent());
    }

    @Test
    void shouldRejectEmptyContent() {

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "T", "   ",
                List.of("A", "B"), "A", validClassification(), null
        );

        assertThrows(QuestionValidationException.class, () -> new ContentValidationFilter().process(request));
    }

    @Test
    void shouldRejectFewerThanTwoOptions() {

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "T", "Contenido suficientemente largo para pasar la validación",
                List.of("Única opción"), "Única opción", validClassification(), null
        );

        assertThrows(QuestionValidationException.class, () -> new OptionsValidationFilter().process(request));
    }

    @Test
    void shouldRejectDuplicatedOptions() {

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "T", "Contenido suficientemente largo para pasar la validación",
                List.of("A", "A"), "A", validClassification(), null
        );

        assertThrows(QuestionValidationException.class, () -> new OptionsValidationFilter().process(request));
    }

    @Test
    void shouldRejectWhenClassificationIsMissingEntirely() {

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "T", "Contenido suficientemente largo para pasar la validación",
                List.of("A", "B"), "A", null, null
        );

        assertThrows(QuestionValidationException.class, () -> new ClassificationFilter().process(request));
    }

    @Test
    void shouldRejectWhenAnySingleClassificationFieldIsBlank() {

        QuestionClassification withoutDifficultyLevel =
                new QuestionClassification("Arquitectura de software", "  ", "Patrones de diseño");

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "T", "Contenido suficientemente largo para pasar la validación",
                List.of("A", "B"), "A", withoutDifficultyLevel, null
        );

        assertThrows(QuestionValidationException.class, () -> new ClassificationFilter().process(request));
    }

    @Test
    void shouldRejectCorrectAnswerNotInOptions() {

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "T", "Contenido suficientemente largo para pasar la validación",
                List.of("A", "B"), "C", validClassification(), null
        );

        assertThrows(QuestionValidationException.class, () -> new CorrectAnswerValidationFilter().process(request));
    }
}
