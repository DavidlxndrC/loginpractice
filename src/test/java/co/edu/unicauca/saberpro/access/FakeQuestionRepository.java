package co.edu.unicauca.saberpro.access;

import co.edu.unicauca.saberpro.domain.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeQuestionRepository implements IQuestionRepository {

    private final Map<Integer, Question> questions = new HashMap<>();

    @Override
    public void save(Question question) {
        questions.put(question.getId(), question);
    }

    @Override
    public Optional<Question> findById(int id) {
        return Optional.ofNullable(questions.get(id));
    }

    @Override
    public List<Question> findAll() {
        return List.copyOf(questions.values());
    }

    @Override
    public void update(Question question) {
        questions.put(question.getId(), question);
    }

    public int size() {
        return questions.size();
    }
}
