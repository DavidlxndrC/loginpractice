package co.edu.unicauca.saberpro.decorator;

/**
 * Decorador: marca la pregunta como de alta dificultad y multiplica su puntaje.
 */
public class HighDifficultyQuestion extends QuestionDecorator {

    private final double multiplier;

    public HighDifficultyQuestion(QuestionComponent wrapped) {
        this(wrapped, 2.0);
    }

    public HighDifficultyQuestion(QuestionComponent wrapped, double multiplier) {
        super(wrapped);
        if (multiplier <= 0) {
            throw new IllegalArgumentException("El multiplicador de puntaje debe ser positivo.");
        }
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public double getMaxScore() {
        return wrapped.getMaxScore() * multiplier;
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " [Alta dificultad x" + multiplier + "]";
    }
}
