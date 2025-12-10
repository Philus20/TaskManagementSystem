package services;

import interfaces.IdGenerator;

public class GenerateUserId  implements IdGenerator{

    private static int counter = 0;

    @Override
    public String generate() {
        return "U" + String.format("%04d", counter++);
    }
}
