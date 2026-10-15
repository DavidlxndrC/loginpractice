package co.edu.unicauca.saberpro.templatemethod;

/**
 * Resultado de evaluar una respuesta.
 */
public class EvaluationResult {

    private final boolean formatValid;
    private final double score;
    private final double maxScore;
    private final String feedback;

    public EvaluationResult(boolean formatValid, double score, double maxScore, String feedback) {
        this.formatValid = formatValid;
        this.score = score;
        this.maxScore = maxScore;
        this.feedback = feedback;
    }

    public static EvaluationResult invalidFormat(double maxScore, String feedback) {
        return new EvaluationResult(false, 0, maxScore, feedback);
    }

    public boolean isFormatValid() {
        return formatValid;
    }

    public double getScore() {
        return score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public boolean isFullScore() {
        return formatValid && score >= maxScore;
    }

    public String getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "Puntaje: " + score + " / " + maxScore + "\n" + feedback;
    }
}
