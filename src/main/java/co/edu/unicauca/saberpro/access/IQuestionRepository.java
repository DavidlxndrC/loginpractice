package co.edu.unicauca.saberpro.access;

import co.edu.unicauca.saberpro.domain.model.Question;

import java.util.List;
import java.util.Optional;

public interface IQuestionRepository {

    void save(Question question);

    Optional<Question> findById(int id);

    List<Question> findAll();

    void update(Question question);
}
