package co.edu.unicauca.saberpro.facade;

import co.edu.unicauca.saberpro.adapter.QuestionProvider;
import co.edu.unicauca.saberpro.builder.IQuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.decorator.BasicQuestion;
import co.edu.unicauca.saberpro.decorator.FeedbackQuestion;
import co.edu.unicauca.saberpro.decorator.HighDifficultyQuestion;
import co.edu.unicauca.saberpro.decorator.QuestionComponent;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionType;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;
import co.edu.unicauca.saberpro.infra.IObserver;
import co.edu.unicauca.saberpro.service.QuestionService;
import co.edu.unicauca.saberpro.templatemethod.CaseStudyEvaluator;
import co.edu.unicauca.saberpro.templatemethod.EvaluationResult;
import co.edu.unicauca.saberpro.templatemethod.MultipleChoiceEvaluator;
import co.edu.unicauca.saberpro.templatemethod.QuestionEvaluator;
import co.edu.unicauca.saberpro.templatemethod.TrueFalseEvaluator;
import co.edu.unicauca.saberpro.validator.QuestionValidator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Fachada unificada del Banco de Preguntas Saber PRO (patrón Facade).
 *
 * La capa de presentación (GUI o Main) solo habla con esta clase; ella
 * coordina los subsistemas internos: servicio/repositorio, validador,
 * builder, importador externo (Adapter), estados (State), evaluadores
 * (Template Method) y decoradores (Decorator).
 */
public class QuestionBankFacade {

    private final QuestionService questionService;
    private final QuestionValidator validator;
    private final QuestionProvider externalProvider;
    private final Map<QuestionType, QuestionEvaluator> evaluators = new EnumMap<>(QuestionType.class);

    public QuestionBankFacade(QuestionService questionService, QuestionValidator validator,
                              QuestionProvider externalProvider) {

        this.questionService = questionService;
        this.validator = validator;
        this.externalProvider = externalProvider;

        evaluators.put(QuestionType.MULTIPLE_CHOICE, new MultipleChoiceEvaluator());
        evaluators.put(QuestionType.TRUE_FALSE, new TrueFalseEvaluator());
        evaluators.put(QuestionType.CASE_STUDY, new CaseStudyEvaluator());
    }

    // ------------------------------------------------------------- consulta

    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    public List<Question> getQuestionsByStatus(StatusQuestion status) {
        return questionService.getAllQuestions().stream()
                .filter(question -> question.getStatus() == status)
                .toList();
    }

    public Optional<Question> findQuestion(int id) {
        return questionService.getQuestionById(id);
    }

    // ------------------------------------------------- creación y edición

    /**
     * Crea una pregunta nueva (en Borrador) a partir de los datos capturados.
     *
     * @throws co.edu.unicauca.saberpro.validator.QuestionValidationException si incumple las reglas
     */
    public Question createQuestion(QuestionForm form) {
        return questionService.addQuestion(buildQuestion(form));
    }

    /**
     * Edita una pregunta. Solo es posible en Borrador (lo decide el estado).
     */
    public void updateQuestion(int id, QuestionForm form) {
        questionService.updateContent(id, buildQuestion(form));
    }

    // -------------------------------------------------- ciclo de vida (State)

    /** Valida la pregunta y la envía a revisión. */
    public void submitForReview(int id) {

        Question question = questionService.getQuestionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada: " + id));
        validator.validateOrThrow(question);

        questionService.applyTransition(id, Question::submitForReview);
    }

    public void startReview(int id) {
        questionService.applyTransition(id, Question::startReview);
    }

    public void approve(int id) {
        questionService.applyTransition(id, Question::approve);
    }

    public void requestCorrections(int id) {
        questionService.applyTransition(id, Question::requestCorrections);
    }

    public void publish(int id) {
        questionService.applyTransition(id, Question::publish);
    }

    // -------------------------------------------------- importación (Adapter)

    /**
     * Importa las preguntas del proveedor externo como borradores. Las que
     * ya existen (mismo enunciado) se omiten, y las inválidas se reportan.
     */
    public ImportResult importExternalQuestions() {

        List<Question> candidates = externalProvider.getQuestions();
        List<String> errors = new ArrayList<>(externalProvider.getErrors());

        int imported = 0;
        int duplicates = 0;

        for (Question candidate : candidates) {
            if (alreadyExists(candidate)) {
                duplicates++;
            } else {
                questionService.addQuestion(candidate);
                imported++;
            }
        }

        return new ImportResult(imported, duplicates, errors);
    }

    private boolean alreadyExists(Question candidate) {
        return questionService.getAllQuestions().stream()
                .anyMatch(existing -> existing.getStatement().equalsIgnoreCase(candidate.getStatement()));
    }

    // ------------------------------- evaluación (Template Method + Decorator)

    /**
     * Evalúa la respuesta a una pregunta publicada (simulacro). Las
     * características opcionales se agregan envolviendo la pregunta con decoradores.
     *
     * @throws IllegalStateException si la pregunta no está publicada
     */
    public EvaluationResult evaluateAnswer(int id, String answer, boolean highDifficulty, boolean withFeedback) {

        Question question = questionService.getQuestionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada: " + id));

        if (question.getStatus() != StatusQuestion.PUBLISHED) {
            throw new IllegalStateException("Solo se pueden evaluar preguntas publicadas.");
        }

        QuestionComponent component = new BasicQuestion(question);
        if (highDifficulty) {
            component = new HighDifficultyQuestion(component);
        }
        if (withFeedback) {
            component = new FeedbackQuestion(component);
        }

        return evaluators.get(question.getType()).evaluate(component, answer);
    }

    // ------------------------------------------------------ observadores

    public void attach(IObserver observer) {
        questionService.attach(observer);
    }

    public void detach(IObserver observer) {
        questionService.detach(observer);
    }

    // ---------------------------------------------------------- internos

    private Question buildQuestion(QuestionForm form) {

        IQuestionBuilder builder = new QuestionBuilder(validator)
                .setType(form.type())
                .setName(form.name())
                .setContext(form.context())
                .setStatement(form.statement());

        for (int i = 0; i < form.options().size(); i++) {
            builder.addOption(form.options().get(i), i == form.correctIndex());
        }

        return builder
                .setJustification(form.justification())
                .setClassification(form.classification())
                .build();
    }
}
