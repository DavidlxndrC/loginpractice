package co.edu.unicauca.saberpro.domain.model;

/**
 * Clasificación de una pregunta: competencia evaluada, nivel de dificultad y categoría.
 */
public class QuestionClassification {

    private final String competency;
    private final String difficultyLevel;
    private final String category;

    public QuestionClassification(String competency, String difficultyLevel, String category) {
        this.competency = competency == null ? "" : competency.trim();
        this.difficultyLevel = difficultyLevel == null ? "" : difficultyLevel.trim();
        this.category = category == null ? "" : category.trim();
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
