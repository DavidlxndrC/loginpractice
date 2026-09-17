package com.loginpractice.microkernel.pipeline;

import java.util.List;

// Su responsabilidad es conectar los filtros y transportar el resultado de un filtro al siguiente.
public class Pipeline<T> {

    private final List<Filter<T>> filters;

    public Pipeline(List<Filter<T>> filters) {
        this.filters = filters;
    }

    public T execute(T input) {

        T result = input;

        for (Filter<T> filter : filters) {
            result = filter.process(result);
        }

        return result;
    }
}
