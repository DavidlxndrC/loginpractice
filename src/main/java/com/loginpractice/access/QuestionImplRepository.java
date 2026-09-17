package com.loginpractice.access;

import com.loginpractice.domain.Question;
import com.loginpractice.domain.QuestionDistractors;
import com.loginpractice.domain.StatusQuestion;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QuestionImplRepository implements IQuestionRepository {

    private final Map<Integer, Question> questions = new LinkedHashMap<>();

    public QuestionImplRepository() {
        seed();
    }

    private void seed() {

        save(new Question(
                1, "Pregunta sobre DDD", "¿Cuál es el objetivo principal de DDD?",
                new QuestionDistractors(List.of(
                        "A. Diseñar bases de datos",
                        "B. Modelar el dominio del negocio",
                        "C. Eliminar UML",
                        "D. Crear interfaces gráficas")),
                "B", StatusQuestion.DRAFT
        ));

        save(new Question(
                2, "Pregunta sobre MVC", "¿Qué componente notifica cambios a las vistas en el patrón Observer?",
                new QuestionDistractors(List.of(
                        "A. El Controller",
                        "B. El Subject",
                        "C. El Repository",
                        "D. El Observer")),
                "B", StatusQuestion.PENDING_REVIEW
        ));

        save(new Question(
                3, "Pregunta sobre capas", "¿Qué capa es responsable de la persistencia de información?",
                new QuestionDistractors(List.of(
                        "A. Presentación",
                        "B. Dominio",
                        "C. Acceso a datos",
                        "D. Transversal")),
                "C", StatusQuestion.DELETED
        ));
    }

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
}

