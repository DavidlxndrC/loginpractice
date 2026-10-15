package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.adapter.ExternalQuestionAdapter;
import co.edu.unicauca.saberpro.adapter.ExternalQuestionService;
import co.edu.unicauca.saberpro.adapter.QuestionProvider;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionType;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExternalQuestionAdapterTest {

    private ExternalQuestionAdapter adapterFor(String json) {
        return new ExternalQuestionAdapter(new ExternalQuestionService() {
            @Override
            public String fetchQuestionsJson() {
                return json;
            }
        });
    }

    @Test
    void shouldBeUsableAsAQuestionProvider() {

        QuestionProvider provider = new ExternalQuestionAdapter(new ExternalQuestionService());

        assertFalse(provider.getQuestions().isEmpty());
    }

    @Test
    void shouldConvertExternalJsonIntoBankQuestions() {

        QuestionProvider provider = new ExternalQuestionAdapter(new ExternalQuestionService());

        List<Question> questions = provider.getQuestions();

        // el servicio de ejemplo trae 3 entradas válidas y 1 inválida
        assertEquals(3, questions.size());
        assertEquals(QuestionType.MULTIPLE_CHOICE, questions.get(0).getType());
        assertEquals("Garantizar una única instancia de una clase", questions.get(0).getCorrectOption().getText());
        assertEquals(QuestionType.TRUE_FALSE, questions.get(1).getType());
        assertEquals(QuestionType.CASE_STUDY, questions.get(2).getType());
        assertFalse(questions.get(2).getContext().isEmpty());
        assertEquals("Diseño de software", questions.get(0).getClassification().getCompetency());
    }

    @Test
    void importedQuestionsShouldStartAsDraft() {

        for (Question question : new ExternalQuestionAdapter(new ExternalQuestionService()).getQuestions()) {
            assertEquals(StatusQuestion.DRAFT, question.getStatus());
        }
    }

    @Test
    void shouldSkipInvalidEntriesAndReportThem() {

        ExternalQuestionAdapter adapter = new ExternalQuestionAdapter(new ExternalQuestionService());

        adapter.getQuestions();

        assertEquals(1, adapter.getErrors().size());
        assertTrue(adapter.getErrors().get(0).contains("EXT-004"));
    }

    @Test
    void shouldReportUnknownTypes() {

        ExternalQuestionAdapter adapter = adapterFor("""
                [{"codigo":"X-1","tipo":"ENSAYO","titulo":"t","enunciado":"e","alternativas":["a","b","c","d"],"clave":"A"}]
                """);

        assertTrue(adapter.getQuestions().isEmpty());
        assertTrue(adapter.getErrors().get(0).contains("tipo desconocido"));
    }

    @Test
    void shouldReportMissingAnswerKey() {

        ExternalQuestionAdapter adapter = adapterFor("""
                [{"codigo":"X-2","tipo":"SELECCION_MULTIPLE","titulo":"t","enunciado":"e","alternativas":["a","b","c","d"]}]
                """);

        assertTrue(adapter.getQuestions().isEmpty());
        assertEquals(1, adapter.getErrors().size());
    }

    @Test
    void shouldReportUnreadableJsonWithoutThrowing() {

        ExternalQuestionAdapter adapter = adapterFor("esto no es json");

        assertTrue(adapter.getQuestions().isEmpty());
        assertEquals(1, adapter.getErrors().size());
    }

    @Test
    void errorsShouldBeResetOnEachRead() {

        ExternalQuestionAdapter adapter = new ExternalQuestionAdapter(new ExternalQuestionService());

        adapter.getQuestions();
        adapter.getQuestions();

        assertEquals(1, adapter.getErrors().size());
    }
}
