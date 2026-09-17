package com.loginpractice.presentation.menu;

import com.loginpractice.domain.User;

public class StudentMenu implements RoleMenu {

    @Override
    public String getWelcomeMessage(User user) {
        return "Bienvenido, " + user.getFullName() + " (Estudiante)";
    }

    @Override
    public String[] getOptions() {
        return new String[] {
                "Presentar evaluación",
                "Ver mis resultados",
                "Ver mis cursos inscritos"
        };
    }
}