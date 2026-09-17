package com.loginpractice;

import com.loginpractice.microkernel.pipeline.QuestionValidationException;
import com.loginpractice.microkernel.common.entities.GeneratedQuestion;
import com.loginpractice.microkernel.common.entities.QuestionClassification;
import com.loginpractice.microkernel.common.entities.QuestionRequest;
import com.loginpractice.microkernel.plugins.CaseStudyQuestionPlugin;
import com.loginpractice.microkernel.plugins.MultimediaQuestionPlugin;
import com.loginpractice.microkernel.plugins.MultipleChoiceQuestionPlugin;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionPluginTest {

    private QuestionClassification validClassification() {
        return new QuestionClassification("Arquitectura de software", "Media", "Patrones de diseño");
    }

    @Test
    void multipleChoicePluginShouldGenerateQuestionWhenRequestIsValid() {

        MultipleChoiceQuestionPlugin plugin = new MultipleChoiceQuestionPlugin();

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "Patrones arquitectónicos",
                "¿Qué patrón permite añadir funciones sin tocar el núcleo?",
                List.of("Microkernel", "Monolito", "Capas", "MVC"), "Microkernel",
                validClassification(), null
        );

        GeneratedQuestion generated = plugin.generate(request);

        assertEquals("MULTIPLE_CHOICE", generated.getType());
        assertEquals("Microkernel", generated.getCorrectAnswer());
        assertTrue(plugin.supports("multiple_choice"));
    }

    @Test
    void multipleChoicePluginShouldRejectInvalidRequestThroughPipeline() {

        MultipleChoiceQuestionPlugin plugin = new MultipleChoiceQuestionPlugin();

        QuestionRequest invalidRequest = new QuestionRequest(
                "MULTIPLE_CHOICE", "T", "corto",
                List.of("A"), "A", null, null
        );

        assertThrows(QuestionValidationException.class, () -> plugin.generate(invalidRequest));
    }

    @Test
    void casePluginShouldGenerateQuestionWithCasePrefix() {

        CaseStudyQuestionPlugin plugin = new CaseStudyQuestionPlugin();

        QuestionRequest request = new QuestionRequest(
                "CASE_STUDY", "Caso de arquitectura",
                "Una empresa de educación superior necesita incorporar progresivamente nuevos tipos "
                        + "de pregunta a su banco de preguntas, sin modificar el núcleo del sistema cada vez "
                        + "que aparece un nuevo tipo. ¿Qué patrón arquitectónico resolvería mejor este problema?",
                List.of("Microkernel", "Monolito"), "Microkernel", validClassification(), null
        );

        GeneratedQuestion generated = plugin.generate(request);

        assertTrue(generated.getContent().startsWith("[CASO]"));
    }

    @Test
    void casePluginShouldRejectScenarioShorterThanMinimumLength() {

        CaseStudyQuestionPlugin plugin = new CaseStudyQuestionPlugin();

        QuestionRequest request = new QuestionRequest(
                "CASE_STUDY", "Caso", "Un caso demasiado corto para ser un escenario real.",
                List.of("A", "B"), "A", validClassification(), null
        );

        assertThrows(QuestionValidationException.class, () -> plugin.generate(request));
    }

    @Test
    void casePluginShouldRejectEmptyCorrectAnswer() {

        CaseStudyQuestionPlugin plugin = new CaseStudyQuestionPlugin();

        QuestionRequest request = new QuestionRequest(
                "CASE_STUDY", "Caso", "Contenido del caso lo suficientemente largo",
                List.of("A", "B"), "", validClassification(), null
        );

        assertThrows(QuestionValidationException.class, () -> plugin.generate(request));
    }

    @Test
    void casePluginShouldReuseOptionsValidationFilterAndRejectDuplicatedOptions() {

        CaseStudyQuestionPlugin plugin = new CaseStudyQuestionPlugin();

        QuestionRequest request = new QuestionRequest(
                "CASE_STUDY", "Caso", "Contenido del caso lo suficientemente largo",
                List.of("Microkernel", "Microkernel"), "Microkernel", validClassification(), null
        );

        assertThrows(QuestionValidationException.class, () -> plugin.generate(request));
    }

    @Test
    void multimediaPluginShouldRequireMediaUrl() {

        MultimediaQuestionPlugin plugin = new MultimediaQuestionPlugin();

        QuestionRequest request = new QuestionRequest(
                "MULTIMEDIA", "Pregunta con video", "Observe el video y responda",
                List.of("A", "B"), "A", validClassification(), null
        );

        assertThrows(QuestionValidationException.class, () -> plugin.generate(request));
    }

    @Test
    void multimediaPluginShouldReuseContentValidationFilterAndRejectShortContent() {

        MultimediaQuestionPlugin plugin = new MultimediaQuestionPlugin();

        QuestionRequest request = new QuestionRequest(
                "MULTIMEDIA", "Pregunta con video", "corto",
                List.of("A", "B"), "A", validClassification(), "https://ejemplo.com/video.mp4"
        );

        assertThrows(QuestionValidationException.class, () -> plugin.generate(request));
    }

    @Test
    void multimediaPluginShouldGenerateQuestionWhenMediaUrlIsPresent() {

        MultimediaQuestionPlugin plugin = new MultimediaQuestionPlugin();

        QuestionRequest request = new QuestionRequest(
                "MULTIMEDIA", "Pregunta con video", "Observe el video y responda",
                List.of("A", "B"), "A", validClassification(), "https://ejemplo.com/video.mp4"
        );

        GeneratedQuestion generated = plugin.generate(request);

        assertTrue(generated.getContent().contains("https://ejemplo.com/video.mp4"));
    }
}
