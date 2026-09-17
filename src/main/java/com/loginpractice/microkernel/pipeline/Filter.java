package com.loginpractice.microkernel.pipeline;

// Cada filtro recibe una entrada, la valida/transforma y produce una salida para el siguiente filtro de la tubería.
public interface Filter<T> {

    T process(T input);
}
