package co.edu.unicauca.saberpro.builder;

import co.edu.unicauca.saberpro.domain.model.Context;
import co.edu.unicauca.saberpro.domain.model.Option;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.QuestionType;
import co.edu.unicauca.saberpro.validator.QuestionValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder concreto de Question. Tras cada build() queda listo para armar otra pregunta.
 */
public class QuestionBuilder implements IQuestionBuilder {

    private final QuestionValidator validator;

    private String name;
    private QuestionType type;
    private String contextText;
    private String statement;
    private List<Option> options;
    private String justification;
    private QuestionClassification classification;

    public QuestionBuilder() {
        this(new QuestionValidator());
    }

    public QuestionBuilder(QuestionValidator validator) {
        this.validator = validator;
        reset();
    }

    @Override
    public IQuestionBuilder reset() {
        name = null;
        type = QuestionType.MULTIPLE_CHOICE;
        contextText = "";
        statement = null;
        options = new ArrayList<>();
        justification = "";
        classification = new QuestionClassification("", "", "");
        return this;
    }

    @Override
    public IQuestionBuilder setName(String name) {
        this.name = name == null ? null : name.trim();
        return this;
    }

    @Override
    public IQuestionBuilder setType(QuestionType type) {
        this.type = type;
        return this;
    }

    @Override
    public IQuestionBuilder setContext(String contextText) {
        this.contextText = contextText;
        return this;
    }

    @Override
    public IQuestionBuilder setStatement(String statement) {
        this.statement = statement == null ? null : statement.trim();
        return this;
    }

    @Override
    public IQuestionBuilder addOption(String text, boolean correct) {
        String label = String.valueOf((char) ('A' + options.size()));
        options.add(new Option(label, text == null ? "" : text.trim(), correct));
        return this;
    }

    @Override
    public IQuestionBuilder setJustification(String justification) {
        this.justification = justification == null ? "" : justification.trim();
        return this;
    }

    @Override
    public IQuestionBuilder setClassification(QuestionClassification classification) {
        this.classification = classification;
        return this;
    }

    @Override
    public Question build() {

        Question question = new Question(name, type, new Context(contextText), statement,
                options, justification, classification);

        reset();

        validator.validateOrThrow(question);

        return question;
    }
}
