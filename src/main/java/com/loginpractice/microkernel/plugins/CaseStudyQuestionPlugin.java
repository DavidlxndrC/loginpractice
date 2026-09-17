package com.loginpractice.microkernel.plugins;

import com.loginpractice.microkernel.pipeline.ContentValidationFilter;
import com.loginpractice.microkernel.pipeline.CorrectAnswerValidationFilter;
import com.loginpractice.microkernel.pipeline.OptionsValidationFilter;
import com.loginpractice.microkernel.pipeline.Pipeline;
import com.loginpractice.microkernel.pipeline.QuestionValidationException;
import com.loginpractice.microkernel.common.entities.GeneratedQuestion;
import com.loginpractice.microkernel.common.interfaces.IQuestionPlugin;
import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.util.List;

/**
 Plugin que genera preguntas de análisis de casos (preguntas basadas en escenarios).

 Reusa ContentValidationFilter, OptionsValidationFilter y CorrectAnswerValidationFilter
 y agrega su propia regla específica: que el escenario del caso esté suficientemente desarrollado
 */
public class CaseStudyQuestionPlugin implements IQuestionPlugin {

    private static final int MIN_CASE_LENGTH = 100;

    private final Pipeline<QuestionRequest> validationPipeline = new Pipeline<>(List.of(
            new ContentValidationFilter(),
            new OptionsValidationFilter(),
            new CorrectAnswerValidationFilter()
    ));

    @Override
    public String getName() {
        return "case-study-question-plugin";
    }

    @Override
    public boolean supports(String type) {
        return "CASE_STUDY".equalsIgnoreCase(type);
    }

    @Override
    public GeneratedQuestion generate(QuestionRequest request) {

        System.out.println("Generando pregunta de análisis de caso...");

        QuestionRequest validated = validationPipeline.execute(request);

        if (validated.getContent().trim().length() < MIN_CASE_LENGTH) {
            throw new QuestionValidationException(List.of(
                    "El caso debe describir un escenario suficientemente desarrollado (mínimo "
                            + MIN_CASE_LENGTH + " caracteres)."
            ));
        }

        String content = "[CASO] " + validated.getContent();

        return new GeneratedQuestion(
                validated.getTitle(),
                content,
                "CASE_STUDY",
                validated.getOptions(),
                validated.getCorrectAnswer(),
                validated.getClassification()
        );
    }
}
