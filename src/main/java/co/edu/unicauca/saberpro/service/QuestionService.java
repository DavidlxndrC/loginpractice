package co.edu.unicauca.saberpro.service;

import co.edu.unicauca.saberpro.access.IQuestionRepository;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.infra.Subject;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class QuestionService extends Subject {

    private final IQuestionRepository questionRepository;

    public QuestionService(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(int id) {
        return questionRepository.findById(id);
    }

    /**
     * Guarda una pregunta nueva (que nace en Borrador) asignándole su id.
     */
    public Question addQuestion(Question question) {

        int newId = getAllQuestions().stream().mapToInt(Question::getId).max().orElse(0) + 1;

        question.setId(newId);
        questionRepository.save(question);

        notifyObservers();

        return question;
    }

    /**
     * Reemplaza el contenido de una pregunta. Lo permite el estado actual
     * de la pregunta (solo Borrador); en cualquier otro estado lanza IllegalStateException.
     */
    public void updateContent(int id, Question newContent) {

        Question question = findOrThrow(id);

        question.edit(newContent);
        questionRepository.update(question);

        notifyObservers();
    }

    /**
     * Aplica una operación del ciclo de vida (enviar a revisión, aprobar, etc.)
     * y notifica a los observadores. Si el estado actual no la permite,
     * la pregunta no cambia y se propaga IllegalStateException.
     */
    public void applyTransition(int id, Consumer<Question> transition) {

        Question question = findOrThrow(id);

        transition.accept(question);
        questionRepository.update(question);

        notifyObservers();
    }

    private Question findOrThrow(int id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada: " + id));
    }
}
