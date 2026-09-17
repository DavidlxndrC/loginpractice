package com.loginpractice;

import com.loginpractice.access.FakeQuestionRepository;
import com.loginpractice.domain.Question;
import com.loginpractice.microkernel.core.QuestionMicrokernel;
import com.loginpractice.microkernel.common.entities.QuestionClassification;
import com.loginpractice.microkernel.common.entities.QuestionRequest;
import com.loginpractice.service.QuestionGeneratorService;
import com.loginpractice.service.QuestionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionGeneratorServiceTest {

    private QuestionGeneratorService generatorService;
    private FakeQuestionRepository fakeQuestionRepository;

    @BeforeEach
    void setUp() {
        fakeQuestionRepository = new FakeQuestionRepository();
        QuestionService questionService = new QuestionService(fakeQuestionRepository);
        generatorService = new QuestionGeneratorService(new QuestionMicrokernel(), questionService);
    }

    @Test
    void shouldGenerateAndPersistQuestionInTheExistingBank() {

        QuestionClassification classification =
                new QuestionClassification("Arquitectura de software", "Media", "Patrones de diseño");

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "Pregunta generada",
                "¿Cuál es el propósito del patrón Tuberías y Filtros?",
                List.of("Procesar datos en etapas encadenadas", "Cifrar contraseñas",
                        "Enrutar peticiones HTTP", "Ninguna de las anteriores"),
                "Procesar datos en etapas encadenadas", classification, null
        );

        Question created = generatorService.generateAndSave(request);

        assertEquals(1, fakeQuestionRepository.size());
        assertTrue(created.getStatement().contains("MULTIPLE_CHOICE"));
        assertTrue(created.getStatement().contains("Arquitectura de software"));
        assertEquals("Procesar datos en etapas encadenadas", created.getCorrectAnswer());
    }

    @Test
    void shouldExposeTypesRegisteredByTheMicrokernel() {
        assertTrue(generatorService.getAvailableTypes().contains("MULTIPLE_CHOICE"));
    }
}
