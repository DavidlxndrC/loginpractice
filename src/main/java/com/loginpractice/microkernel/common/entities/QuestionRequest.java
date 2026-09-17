package com.loginpractice.microkernel.common.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos de entrada que un plugin (y el pipeline de validación) necesita
 * para generar una pregunta.
 */
public class QuestionRequest {

    private final String type;
    private final String title;
    private final String content;
    private final List<String> options;
    private final String correctAnswer;
    private final QuestionClassification classification;
    private final String mediaUrl;

    public QuestionRequest(String type, String title, String content, List<String> options,
                            String correctAnswer, QuestionClassification classification, String mediaUrl) {

        this.type = type;
        this.title = title;
        this.content = content;
        this.options = (options == null) ? new ArrayList<>() : new ArrayList<>(options);
        this.correctAnswer = correctAnswer;
        this.classification = classification;
        this.mediaUrl = mediaUrl;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    public String getMediaUrl() {
        return mediaUrl;
    }
}
