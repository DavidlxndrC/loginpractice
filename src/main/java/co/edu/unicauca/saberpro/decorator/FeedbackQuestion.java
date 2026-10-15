package co.edu.unicauca.saberpro.decorator;

/**
 * Decorador: adjunta una explicación técnica detallada que se muestra junto
 * al resultado de la evaluación. Si no se indica una explicación propia, usa
 * la justificación de la pregunta.
 */
public class FeedbackQuestion extends QuestionDecorator {

    private final String explanation;

    public FeedbackQuestion(QuestionComponent wrapped) {
        this(wrapped, wrapped.getQuestion().getJustification());
    }

    public FeedbackQuestion(QuestionComponent wrapped, String explanation) {
        super(wrapped);
        this.explanation = explanation == null ? "" : explanation.trim();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " [Con retroalimentación]";
    }

    @Override
    public String getFeedback() {
        return (wrapped.getFeedback() + " " + explanation).trim();
    }
}
