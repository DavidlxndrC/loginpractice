package com.loginpractice.microkernel.common.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Resultado producido por un {@link QuestionPlugin}.
 *
 * Es un "borrador" de pregunta: title, content, type, options, correctAnswer
 * y classification. Deliberadamente NO tiene id propio: la identidad de una
 * pregunta la asigna una sola vez QuestionService.createQuestion(...) cuando
 * la persiste en el banco real. Así se evita que exista un id "del plugin"
 * (por ejemplo un UUID) que no tenga relación con el id que el resto de la
 * aplicación usa para esa misma pregunta.
 */
public class GeneratedQuestion {

    private final String title;
    private final String content;
    private final String type;
    private final List<String> options;
    private final String correctAnswer;
    private final QuestionClassification classification;

    public GeneratedQuestion(String title, String content, String type,
                              List<String> options, String correctAnswer, QuestionClassification classification) {

        this.title = title;
        this.content = content;
        this.type = type;
        this.options = (options == null) ? new ArrayList<>() : new ArrayList<>(options);
        this.correctAnswer = correctAnswer;
        this.classification = classification;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public QuestionClassification getClassification() {
        return classification;
    }
}
