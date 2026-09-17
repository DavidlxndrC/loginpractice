package com.loginpractice.presentation.menu;

import com.loginpractice.domain.User;

public class QuestionAuthorMenu implements RoleMenu {

    @Override
    public String getWelcomeMessage(User user) {
        return "Bienvenido, " + user.getFullName() + " (Autor de preguntas)";
    }

    @Override
    public String[] getOptions() {
        return new String[] {
                "Gestionar preguntas",
                "Generar preguntas (Plugins)",
                "Vista de estadísticas",
                "Vista gráfica",
                "Ver estado de revisión",
                "Historial de preguntas enviadas"
        };
    }
}