package validation;

import exception.InvalidInputException;
import interfaces.validation.IValidator;

public class ContactNumValidator implements IValidator<String> {
    public boolean validate(String input) {
        return input.matches("^[89][0-9]{7}$");
    }

    public boolean validateNoNull(String input) throws InvalidInputException {
        if (input == null) throw new InvalidInputException("null input.");
        if (input.isEmpty()) throw new InvalidInputException("empty input.");
        return validate(input);
    }
}
