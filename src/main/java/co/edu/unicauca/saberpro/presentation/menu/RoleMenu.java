package co.edu.unicauca.saberpro.presentation.menu;

import co.edu.unicauca.saberpro.domain.model.User;

public interface RoleMenu {

    String getWelcomeMessage(User user);

    String[] getOptions();
}