package com.loginpractice.access;

public class UserRepositoryFactory {

    private static UserRepositoryFactory instance;

    private UserRepositoryFactory() {
    }

    public static UserRepositoryFactory getInstance() {

        if (instance == null) {
            instance = new UserRepositoryFactory();
        }
        return instance;
    }

    public IUserRepository getRepository(String type) {

        IUserRepository result = null;

        switch (type) {
            case "default":
                result = new UserRepository();
                break;
        }

        return result;
    }
}