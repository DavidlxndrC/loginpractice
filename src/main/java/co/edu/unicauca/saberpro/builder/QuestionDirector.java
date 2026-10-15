package co.edu.unicauca.saberpro.builder;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.QuestionType;

import java.util.List;

/**
 * Director del patrón Builder: conoce las "recetas" para armar cada tipo de
 * pregunta usando cualquier IQuestionBuilder.
 */
public class QuestionDirector {

    private final IQuestionBuilder builder;

    public QuestionDirector(IQuestionBuilder builder) {
        this.builder = builder;
    }

    public Question buildMultipleChoice(String name, String statement, List<String> options,
                                        int correctIndex, String justification,
                                        QuestionClassification classification) {

        return assemble(QuestionType.MULTIPLE_CHOICE, name, "", statement, options, correctIndex,
                justification, classification);
    }

    public Question buildTrueFalse(String name, String statement, boolean answer,
                                   String justification, QuestionClassification classification) {

        return assemble(QuestionType.TRUE_FALSE, name, "", statement,
                List.of("Verdadero", "Falso"), answer ? 0 : 1, justification, classification);
    }

    public Question buildCaseStudy(String name, String context, String statement, List<String> options,
                                   int correctIndex, String justification,
                                   QuestionClassification classification) {

        return assemble(QuestionType.CASE_STUDY, name, context, statement, options, correctIndex,
                justification, classification);
    }

    private Question assemble(QuestionType type, String name, String context, String statement,
                              List<String> options, int correctIndex, String justification,
                              QuestionClassification classification) {

        builder.reset()
                .setType(type)
                .setName(name)
                .setContext(context)
                .setStatement(statement);

        for (int i = 0; i < options.size(); i++) {
            builder.addOption(options.get(i), i == correctIndex);
        }

        return builder.setJustification(justification)
                .setClassification(classification)
                .build();
    }
}
