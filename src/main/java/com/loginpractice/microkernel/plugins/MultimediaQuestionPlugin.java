package com.loginpractice.microkernel.plugins;

import com.loginpractice.microkernel.pipeline.ContentValidationFilter;
import com.loginpractice.microkernel.pipeline.OptionsValidationFilter;
import com.loginpractice.microkernel.pipeline.Pipeline;
import com.loginpractice.microkernel.pipeline.QuestionValidationException;
import com.loginpractice.microkernel.common.entities.GeneratedQuestion;
import com.loginpractice.microkernel.common.interfaces.IQuestionPlugin;
import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.util.List;

/**
 Plugin que genera preguntas con recursos multimedia (imagen, audio o
 video)

 Reusa ContentValidationFilter y OptionsValidationFilter, además,
 agrega su propia regla específica: que exista una URL o ruta del recurso multimedia.
 */
public class MultimediaQuestionPlugin implements IQuestionPlugin {

    private final Pipeline<QuestionRequest> validationPipeline = new Pipeline<>(List.of(
            new ContentValidationFilter(),
            new OptionsValidationFilter()
    ));

    @Override
    public String getName() {
        return "multimedia-question-plugin";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIMEDIA".equalsIgnoreCase(type);
    }

    @Override
    public GeneratedQuestion generate(QuestionRequest request) {

        System.out.println("Generando pregunta con recurso multimedia...");

        QuestionRequest validated = validationPipeline.execute(request);

        if (validated.getMediaUrl() == null || validated.getMediaUrl().isBlank()) {
            throw new QuestionValidationException(
                    List.of("Debe adjuntar la URL o ruta del recurso multimedia (imagen, audio o video).")
            );
        }

        String content = validated.getContent() + " [Recurso: " + validated.getMediaUrl() + "]";

        return new GeneratedQuestion(
                validated.getTitle(),
                content,
                "MULTIMEDIA",
                validated.getOptions(),
                validated.getCorrectAnswer(),
                validated.getClassification()
        );
    }
}
