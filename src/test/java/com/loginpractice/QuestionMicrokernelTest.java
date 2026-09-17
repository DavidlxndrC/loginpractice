package com.loginpractice;

import com.loginpractice.microkernel.core.QuestionMicrokernel;
import com.loginpractice.microkernel.common.entities.GeneratedQuestion;
import com.loginpractice.microkernel.common.entities.QuestionClassification;
import com.loginpractice.microkernel.common.interfaces.IQuestionPlugin;
import com.loginpractice.microkernel.common.entities.QuestionRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionMicrokernelTest {

    private QuestionMicrokernel microkernel;

    private QuestionClassification validClassification() {
        return new QuestionClassification("Arquitectura de software", "Media", "Patrones de diseño");
    }

    @BeforeEach
    void setUp() {
        microkernel = new QuestionMicrokernel();
    }

    @Test
    void shouldRegisterPluginsFromPropertiesFileUsingReflection() {

        Set<String> types = microkernel.getRegisteredTypes();

        assertTrue(types.contains("MULTIPLE_CHOICE"));
        assertTrue(types.contains("CASE_STUDY"));
        assertTrue(types.contains("MULTIMEDIA"));
    }

    @Test
    void shouldReturnPluginForRegisteredType() {

        IQuestionPlugin plugin = microkernel.getPlugin("MULTIPLE_CHOICE");

        assertNotNull(plugin);
        assertTrue(plugin.supports("MULTIPLE_CHOICE"));
    }

    @Test
    void shouldReturnNullForUnregisteredType() {
        assertNull(microkernel.getPlugin("UNKNOWN_TYPE"));
    }

    @Test
    void shouldRegisterGeneratedQuestionUnderTheFinalDomainId() {

        QuestionRequest request = new QuestionRequest(
                "MULTIPLE_CHOICE", "Pregunta de patrones",
                "¿Cuál patrón desacopla el núcleo de las extensiones?",
                List.of("Microkernel", "Singleton", "Facade", "Builder"),
                "Microkernel", validClassification(), null
        );

        GeneratedQuestion generated = microkernel.generateQuestion(request);
        microkernel.registerGenerated(1, generated);

        assertEquals(1, microkernel.getAllGenerated().size());
        assertTrue(microkernel.findGenerated(1).isPresent());
    }

    @Test
    void shouldUnloadPluginFromLifecycle() {

        microkernel.unloadPlugin("MULTIMEDIA");

        assertNull(microkernel.getPlugin("MULTIMEDIA"));
    }

    @Test
    void shouldReloadPluginAfterBeingUnloaded() {

        microkernel.unloadPlugin("MULTIMEDIA");
        microkernel.reloadPlugin("MULTIMEDIA");

        assertNotNull(microkernel.getPlugin("MULTIMEDIA"));
    }

    @Test
    void shouldThrowExceptionWhenNoPluginSupportsTheRequestedType() {

        QuestionRequest request = new QuestionRequest(
                "UNKNOWN_TYPE", "T", "Contenido de prueba suficientemente largo",
                List.of("A", "B"), "A", validClassification(), null
        );

        assertThrows(IllegalArgumentException.class, () -> microkernel.generateQuestion(request));
    }
}
