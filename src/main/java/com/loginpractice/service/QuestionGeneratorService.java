package com.loginpractice.service;

import com.loginpractice.domain.Question;
import com.loginpractice.domain.QuestionDistractors;
import com.loginpractice.microkernel.core.QuestionMicrokernel;
import com.loginpractice.microkernel.common.entities.GeneratedQuestion;
import com.loginpractice.microkernel.common.entities.QuestionClassification;
import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.util.Set;

/**
 Conecta el módulo de generación de preguntas basado en el patrón
 Microkernel con el banco de preguntas ya existente en el proyecto
 (QuestionService / IQuestionRepository).

 De esta forma, toda pregunta generada por un plugin queda disponible de
 inmediato en "Gestionar preguntas", en las vistas de estadísticas y en
 la gráfica (Observer), sin duplicar el almacenamiento del banco de preguntas.
 */
public class QuestionGeneratorService {

    private final QuestionMicrokernel microkernel;
    private final QuestionService questionService;

    public QuestionGeneratorService(QuestionMicrokernel microkernel, QuestionService questionService) {
        this.microkernel = microkernel;
        this.questionService = questionService;
    }

    public Set<String> getAvailableTypes() {
        return microkernel.getRegisteredTypes();
    }

    /**
     * Genera una pregunta a través del plugin correspondiente (que puede
     * incluir su propio pipeline de validación), la persiste en el banco
     * de preguntas del proyecto y registra esa misma pregunta en el
     * núcleo del microkernel usando el id definitivo que le asignó la
     * persistencia (para no manejar dos ids distintos para una misma
     * pregunta).
     */
    public Question generateAndSave(QuestionRequest request) {

        GeneratedQuestion generated = microkernel.generateQuestion(request);

        QuestionClassification classification = generated.getClassification();
        String classificationLabel = classification.getCompetency() + " - " + classification.getDifficultyLevel()
                + " - " + classification.getCategory();

        String statement = "[" + generated.getType() + "] [" + classificationLabel + "] "
                + generated.getContent();

        QuestionDistractors options = new QuestionDistractors(generated.getOptions());

        Question created = questionService.createQuestion(
                generated.getTitle(),
                statement,
                options,
                generated.getCorrectAnswer()
        );

        microkernel.registerGenerated(created.getId(), generated);

        return created;
    }
}
