package com.loginpractice.presentation.menu;

import com.loginpractice.domain.User;

public class ReviewerMenu implements RoleMenu {

    @Override
    public String getWelcomeMessage(User user) {
        return "Bienvenido, " + user.getFullName() + " (Revisor)";
    }

    @Override
    public String[] getOptions() {
        return new String[] {
                "Gestionar preguntas",
                "Vista de estadísticas",
                "Vista gráfica",
                "Revisar preguntas pendientes",
                "Aprobar o rechazar preguntas",
                "Ver historial de revisiones"
        };
    }
}