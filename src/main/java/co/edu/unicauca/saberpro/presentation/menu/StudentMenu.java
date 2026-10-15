package co.edu.unicauca.saberpro.presentation.menu;

import co.edu.unicauca.saberpro.domain.model.User;

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