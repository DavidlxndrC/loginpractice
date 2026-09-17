package com.loginpractice.microkernel.common.entities;

/**
 * Clasificación de una pregunta, tal como la describe el paso "Clasificar
 * competencia" del pipeline del taller: determina la competencia, asigna
 * un nivel de dificultad y establece una categoría.
 */
public class QuestionClassification {

    private final String competency;
    private final String difficultyLevel;
    private final String category;

    public QuestionClassification(String competency, String difficultyLevel, String category) {
        this.competency = competency;
        this.difficultyLevel = difficultyLevel;
        this.category = category;
    }

    public String getCompetency() {
        return competency;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return competency + " / " + difficultyLevel + " / " + category;
    }
}
