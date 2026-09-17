package com.loginpractice.presentation.menu;

import com.loginpractice.domain.User;

public class TeacherMenu implements RoleMenu {

    @Override
    public String getWelcomeMessage(User user) {
        return "Bienvenido, " + user.getFullName() + " (Docente)";
    }

    @Override
    public String[] getOptions() {
        return new String[] {
                "Crear evaluación",
                "Ver resultados de estudiantes",
                "Gestionar mis cursos"
        };
    }
}