package services;

import interfaces.IdGenerator;

public class GenerateProjectId implements IdGenerator {
    private static int counter = 0;

    @Override
    public String generate() {


            return "P" + String.format("%04d", counter++);

    }
    @Override
    public int elementIndex(String id) {
        return Integer.parseInt(id.substring(1));
    }
}
