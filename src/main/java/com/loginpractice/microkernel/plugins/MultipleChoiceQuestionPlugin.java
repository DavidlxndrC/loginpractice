package com.loginpractice.microkernel.plugins;

import com.loginpractice.microkernel.pipeline.ClassificationFilter;
import com.loginpractice.microkernel.pipeline.ContentValidationFilter;
import com.loginpractice.microkernel.pipeline.CorrectAnswerValidationFilter;
import com.loginpractice.microkernel.pipeline.OptionsValidationFilter;
import com.loginpractice.microkernel.pipeline.Pipeline;
import com.loginpractice.microkernel.common.entities.GeneratedQuestion;
import com.loginpractice.microkernel.common.interfaces.QuestionPlugin;
import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.util.List;

/**
 Plugin que genera preguntas de selección múltiple.

 Este es el plugin que integra obligatoriamente el pipeline de
 validación (Tuberías y Filtros) antes de generar
 la pregunta, la solicitud pasa por los cuatro filtros requeridos.
 */
public class MultipleChoiceQuestionPlugin implements QuestionPlugin {

    private final Pipeline<QuestionRequest> validationPipeline = new Pipeline<>(List.of(
            new ContentValidationFilter(),
            new OptionsValidationFilter(),
            new ClassificationFilter(),
            new CorrectAnswerValidationFilter()
    ));

    @Override
    public String getName() {
        return "multiple-choice-question-plugin";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIPLE_CHOICE".equalsIgnoreCase(type);
    }

    @Override
    public GeneratedQuestion generate(QuestionRequest request) {

        System.out.println("Generando pregunta de selección múltiple...");

        QuestionRequest validated = validationPipeline.execute(request);

        return new GeneratedQuestion(
                validated.getTitle(),
                validated.getContent(),
                "MULTIPLE_CHOICE",
                validated.getOptions(),
                validated.getCorrectAnswer(),
                validated.getClassification()
        );
    }
}
