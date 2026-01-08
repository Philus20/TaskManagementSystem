package utils.exceptions;

public class EntityAttributeException extends RuntimeException {
    public EntityAttributeException(String attribute) {
        super(attribute);

        System.out.printf("The %s attribute is null  " ,attribute);


    }
}
