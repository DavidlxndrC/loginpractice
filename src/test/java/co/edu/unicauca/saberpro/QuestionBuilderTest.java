package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.builder.IQuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionDirector;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.QuestionType;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;
import co.edu.unicauca.saberpro.validator.QuestionValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionBuilderTest {

    private IQuestionBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new QuestionBuilder();
    }

    private IQuestionBuilder validBuilder() {
        return builder
                .setName("Pregunta de prueba")
                .setContext("Un sistema usa capas.")
                .setStatement("¿Qué capa persiste los datos?")
                .addOption("Presentación", false)
                .addOption("Dominio", false)
                .addOption("Acceso a datos", true)
                .addOption("Transversal", false)
                .setJustification("La capa de acceso a datos encapsula la persistencia.")
                .setClassification(new QuestionClassification("Arquitectura", "Básico", "Capas"));
    }

    @Test
    void shouldBuildQuestionWithAllItsParts() {

        Question question = validBuilder().build();

        assertEquals("Pregunta de prueba", question.getName());
        assertEquals("Un sistema usa capas.", question.getContext().getText());
        assertEquals("¿Qué capa persiste los datos?", question.getStatement());
        assertEquals(4, question.getOptions().size());
        assertEquals("Acceso a datos", question.getCorrectOption().getText());
        assertEquals("C", question.getCorrectOption().getLabel());
        assertEquals("La capa de acceso a datos encapsula la persistencia.", question.getJustification());
        assertEquals("Arquitectura", question.getClassification().getCompetency());
    }

    @Test
    void shouldAssignLabelsAToDInOrder() {

        Question question = validBuilder().build();

        assertEquals(List.of("A", "B", "C", "D"),
                question.getOptions().stream().map(option -> option.getLabel()).toList());
    }

    @Test
    void newQuestionShouldStartAsDraft() {

        assertEquals(StatusQuestion.DRAFT, validBuilder().build().getStatus());
    }

    @Test
    void shouldRejectFewerThanFourOptions() {

        builder.setName("N").setStatement("S")
                .addOption("a", true).addOption("b", false).addOption("c", false);

        QuestionValidationException ex = assertThrows(QuestionValidationException.class, () -> builder.build());

        assertTrue(ex.getErrors().stream().anyMatch(error -> error.contains("exactamente 4 opciones")));
    }

    @Test
    void shouldRejectMoreThanFourOptions() {

        builder.setName("N").setStatement("S")
                .addOption("a", true).addOption("b", false).addOption("c", false)
                .addOption("d", false).addOption("e", false);

        assertThrows(QuestionValidationException.class, () -> builder.build());
    }

    @Test
    void shouldRejectQuestionWithoutCorrectAnswer() {

        builder.setName("N").setStatement("S")
                .addOption("a", false).addOption("b", false).addOption("c", false).addOption("d", false);

        QuestionValidationException ex = assertThrows(QuestionValidationException.class, () -> builder.build());

        assertTrue(ex.getErrors().stream().anyMatch(error -> error.contains("exactamente 1 respuesta correcta")));
    }

    @Test
    void shouldRejectQuestionWithMoreThanOneCorrectAnswer() {

        builder.setName("N").setStatement("S")
                .addOption("a", true).addOption("b", true).addOption("c", false).addOption("d", false);

        assertThrows(QuestionValidationException.class, () -> builder.build());
    }

    @Test
    void shouldRejectBlankAndDuplicatedOptions() {

        builder.setName("N").setStatement("S")
                .addOption("igual", true).addOption("IGUAL", false).addOption("", false).addOption("d", false);

        QuestionValidationException ex = assertThrows(QuestionValidationException.class, () -> builder.build());

        assertEquals(2, ex.getErrors().size());
    }

    @Test
    void shouldRequireContextInCaseStudy() {

        builder.setType(QuestionType.CASE_STUDY).setName("N").setStatement("S")
                .addOption("a", true).addOption("b", false).addOption("c", false).addOption("d", false);

        QuestionValidationException ex = assertThrows(QuestionValidationException.class, () -> builder.build());

        assertTrue(ex.getErrors().stream().anyMatch(error -> error.contains("contexto")));
    }

    @Test
    void trueFalseQuestionShouldHaveExactlyTwoOptions() {

        Question question = builder.setType(QuestionType.TRUE_FALSE).setName("N").setStatement("S")
                .addOption("Verdadero", true).addOption("Falso", false).build();

        assertEquals(2, question.getOptions().size());
    }

    @Test
    void builderShouldBeReusableAfterBuild() {

        validBuilder().build();

        // tras build() el builder queda limpio: sin pasos nuevos no puede construir nada
        assertThrows(QuestionValidationException.class, () -> builder.build());
        assertDoesNotThrow(() -> validBuilder().build());
    }

    @Test
    void directorShouldBuildEachQuestionType() {

        QuestionDirector director = new QuestionDirector(new QuestionBuilder());
        QuestionClassification classification = new QuestionClassification("c", "n", "k");

        Question multipleChoice = director.buildMultipleChoice("MC", "¿?", List.of("a", "b", "c", "d"), 3, "j", classification);
        Question trueFalse = director.buildTrueFalse("TF", "¿?", false, "j", classification);
        Question caseStudy = director.buildCaseStudy("CS", "caso", "¿?", List.of("a", "b", "c", "d"), 0, "j", classification);

        assertEquals("D", multipleChoice.getCorrectOption().getLabel());
        assertEquals("Falso", trueFalse.getCorrectOption().getText());
        assertEquals(QuestionType.CASE_STUDY, caseStudy.getType());
    }
}
