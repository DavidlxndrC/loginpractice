package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.state.DraftState;
import co.edu.unicauca.saberpro.state.IQuestionState;

import java.util.List;

/**
 * Pregunta del banco (Contexto del patrón State).
 *
 * Se construye con el patrón Builder (QuestionBuilder) y su ciclo de vida lo
 * gobierna el estado actual: cada operación se delega al estado, sin if/else ni switch.
 */
public class Question {

    private int id;
    private String name;
    private QuestionType type;
    private Context context;
    private String statement;
    private List<Option> options;
    private String justification;
    private QuestionClassification classification;
    private IQuestionState state = new DraftState();

    public Question(String name, QuestionType type, Context context, String statement,
                    List<Option> options, String justification, QuestionClassification classification) {

        this.name = name;
        this.type = type;
        this.context = context;
        this.statement = statement;
        this.options = List.copyOf(options);
        this.justification = justification;
        this.classification = classification;
    }

    // ---------------------------------------------------------------- datos

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public QuestionType getType() {
        return type;
    }

    public Context getContext() {
        return context;
    }

    public String getStatement() {
        return statement;
    }

    public List<Option> getOptions() {
        return options;
    }

    public String getJustification() {
        return justification;
    }

    public QuestionClassification getClassification() {
        return classification;
    }

    public Option getCorrectOption() {
        return options.stream().filter(Option::isCorrect).findFirst().orElseThrow();
    }

    // ---------------------------------------------------------------- estado

    public IQuestionState getState() {
        return state;
    }

    /** Lo invocan los estados concretos al hacer una transición. */
    public void setState(IQuestionState state) {
        this.state = state;
    }

    public StatusQuestion getStatus() {
        return state.getStatus();
    }

    /**
     * Reemplaza el contenido por el de otra pregunta ya validada.
     * Solo deben invocarlo los estados que permiten edición (DraftState).
     */
    public void copyContentFrom(Question source) {
        this.name = source.name;
        this.type = source.type;
        this.context = source.context;
        this.statement = source.statement;
        this.options = source.options;
        this.justification = source.justification;
        this.classification = source.classification;
    }

    // ------------------------------------------- operaciones del ciclo de vida

    public void edit(Question newContent) {
        state.edit(this, newContent);
    }

    public void submitForReview() {
        state.submitForReview(this);
    }

    public void startReview() {
        state.startReview(this);
    }

    public void approve() {
        state.approve(this);
    }

    public void requestCorrections() {
        state.requestCorrections(this);
    }

    public void publish() {
        state.publish(this);
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
