package com.loginpractice.presentation.menu;

import com.loginpractice.domain.User;

public interface RoleMenu {

    String getWelcomeMessage(User user);

    String[] getOptions();
}