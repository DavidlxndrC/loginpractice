package co.edu.unicauca.saberpro.presentation.menu;

import co.edu.unicauca.saberpro.domain.model.User;

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