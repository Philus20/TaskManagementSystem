package services;

import interfaces.IdGenerator;

public class GenerateTaskId implements IdGenerator {

    private static int counter = 0;

    @Override
    public String generate() {
        return "T" + String.format("%04d", counter++);
    }

    @Override
    public int elementIndex(String id) {
        return Integer.parseInt(id.substring(1));
    }

}
