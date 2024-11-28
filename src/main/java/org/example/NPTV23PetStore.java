package org.example;

import org.example.interfaces.framework.Factory;
import org.example.interfaces.framework.config.JavaConfiguration;

public class NPTV23PetStore {

    public static void main(String[] args) {
        Factory factory = Factory.getInstance(new JavaConfiguration());
        App app = (App) factory.getObject("app");
        app.run();
    }

}
