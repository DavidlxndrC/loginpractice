package com.loginpractice.microkernel.common.interfaces;

import com.loginpractice.microkernel.common.entities.GeneratedQuestion;
import com.loginpractice.microkernel.common.entities.QuestionRequest;


public interface QuestionPlugin {

    // Nombre identificador del plugin
    String getName();

    // Indica si este plugin sabe generar preguntas del tipo indicado
    boolean supports(String type);

    // Genera una pregunta a partir de la solicitud recibida.
    GeneratedQuestion generate(QuestionRequest request);
}
