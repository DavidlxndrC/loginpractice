package co.edu.unicauca.saberpro.access;

import co.edu.unicauca.saberpro.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionDirector;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QuestionImplRepository implements IQuestionRepository {

    private final Map<Integer, Question> questions = new LinkedHashMap<>();

    public QuestionImplRepository() {
        seed();
    }

    /**
     * Datos de prueba: se arman con el Director/Builder y se llevan a cada
     * estado usando las transiciones reales del patrón State.
     */
    private void seed() {

        QuestionDirector director = new QuestionDirector(new QuestionBuilder());

        Question ddd = director.buildMultipleChoice(
                "Pregunta sobre DDD", "¿Cuál es el objetivo principal de DDD?",
                List.of("Diseñar bases de datos", "Modelar el dominio del negocio",
                        "Eliminar UML", "Crear interfaces gráficas"),
                1, "DDD centra el diseño en el modelo del dominio del negocio.",
                new QuestionClassification("Diseño de software", "Básico", "Arquitectura"));
        save(withId(ddd, 1));

        Question observer = director.buildMultipleChoice(
                "Pregunta sobre Observer", "¿Qué componente notifica cambios a las vistas en el patrón Observer?",
                List.of("El Controller", "El Subject", "El Repository", "El Observer"),
                1, "El Subject mantiene la lista de observadores y los notifica.",
                new QuestionClassification("Diseño de software", "Intermedio", "Patrones de comportamiento"));
        observer.submitForReview();
        save(withId(observer, 2));

        Question layers = director.buildMultipleChoice(
                "Pregunta sobre capas", "¿Qué capa es responsable de la persistencia de información?",
                List.of("Presentación", "Dominio", "Acceso a datos", "Transversal"),
                2, "La capa de acceso a datos encapsula el acceso al almacenamiento.",
                new QuestionClassification("Arquitectura", "Básico", "Arquitectura en capas"));
        layers.submitForReview();
        layers.startReview();
        layers.approve();
        save(withId(layers, 3));

        Question facade = director.buildTrueFalse(
                "Pregunta sobre Facade", "El patrón Facade ofrece una interfaz simplificada a un subsistema.",
                true, "Facade oculta la complejidad interna detrás de una interfaz única.",
                new QuestionClassification("Diseño de software", "Básico", "Patrones estructurales"));
        publishQuestion(facade);
        save(withId(facade, 4));

        Question state = director.buildCaseStudy(
                "Caso sobre State",
                "Un documento pasa por Borrador, Revisión y Publicado, y en cada estado permite acciones distintas. "
                        + "El código actual usa banderas booleanas y if/else anidados para decidir qué se puede hacer.",
                "¿Qué patrón elimina esos condicionales delegando el comportamiento en objetos de estado?",
                List.of("State", "Singleton", "Builder", "Adapter"),
                0, "State encapsula el comportamiento de cada estado en su propia clase.",
                new QuestionClassification("Diseño de software", "Alto", "Patrones de comportamiento"));
        publishQuestion(state);
        save(withId(state, 5));
    }

    private Question withId(Question question, int id) {
        question.setId(id);
        return question;
    }

    private void publishQuestion(Question question) {
        question.submitForReview();
        question.startReview();
        question.approve();
        question.publish();
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
