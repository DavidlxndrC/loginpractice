package co.edu.unicauca.saberpro.presentation.menu;

import co.edu.unicauca.saberpro.domain.model.User;

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