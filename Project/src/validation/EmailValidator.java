package validation;

import exception.InvalidInputException;
import interfaces.validation.IValidator;

public class EmailValidator implements IValidator<String> {
    @Override
    public boolean validate(String input) {
        return input.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    } 

    public boolean validateNoEmpty(String input) throws InvalidInputException {
        if (input == null) throw new InvalidInputException("null input.");
        if (input.isEmpty()) throw new InvalidInputException("empty input.");
        return validate(input);
    } 
}
