# Taller 07-08 – Patrones de diseño (Banco de Preguntas Saber PRO)

Universidad del Cauca · Laboratorio de Ingeniería de Software II

Proyecto Maven (Java 17). Ejecutar: `mvn test` y, para la aplicación, la clase `co.edu.unicauca.saberpro.ApplicationMain`.

## Patrones y dónde están

| Parte | Patrón | Paquete / clases |
|---|---|---|
| 1 | Builder | `builder`: `IQuestionBuilder`, `QuestionBuilder`, `QuestionDirector` |
| 1 | Adapter | `adapter`: `ExternalQuestionService` (Adaptee), `QuestionProvider` (Target), `ExternalQuestionAdapter` |
| 1 | Facade | `facade`: `QuestionBankFacade` (+ `QuestionForm`, `ImportResult`) |
| 2 | State | `state`: `IQuestionState`, `DraftState`, `PendingReviewState`, `ReviewState`, `ApprovedState`, `PublishedState` |
| 2 | Template Method | `templatemethod`: `QuestionEvaluator`, `MultipleChoiceEvaluator`, `TrueFalseEvaluator`, `CaseStudyEvaluator` |
| 2 | Decorator | `decorator`: `QuestionComponent`, `BasicQuestion`, `QuestionDecorator`, `HighDifficultyQuestion`, `FeedbackQuestion` |

Apoyo: `domain.model` (`Question`, `Option`, `Context`, `QuestionClassification`, `QuestionType`, `StatusQuestion`, usuarios y roles),
`validator` (reglas de la pregunta, usadas por el Builder y el Facade), y las capas previas
`presentation`, `service`, `access`, `infra` (Observer).

## Ciclo de vida (State)

Borrador → Pendiente de revisión → En revisión → Aprobada → Publicada
(En revisión → Borrador cuando el revisor solicita correcciones). Las transiciones inválidas lanzan
`IllegalStateException` desde el propio estado; no hay `if/else` ni `switch` en el ciclo de vida.

## Pruebas (JUnit 5)

`QuestionBuilderTest`, `QuestionStateTest`, `QuestionEvaluatorTest`, `QuestionDecoratorTest`,
`ExternalQuestionAdapterTest`, `QuestionBankFacadeTest`, `QuestionServiceTest`, además de las de usuarios/BD ya existentes.

## Repositorio GitHub

Enlace: _(agregar aquí el enlace al repositorio público)_
