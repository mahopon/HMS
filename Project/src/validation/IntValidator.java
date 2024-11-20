package validation;

import exception.InvalidInputException;
import interfaces.validation.IValidator;

public class IntValidator implements IValidator<Integer> {
    @Override
    public boolean validate(Integer input) {
        return input >= 0;
    }

    public boolean validatePos(Integer input) {
        return input > 0;
    } 
    
    public boolean validateNoNull(Integer input) throws InvalidInputException {
        if (input == null) throw new InvalidInputException("Error: null input");
        return validate(input);
    } 
    
    public boolean validatePosNoNull(Integer input) throws InvalidInputException {
        if (input == null) throw new InvalidInputException("Error: null input");
        return validatePos(input);
    } 
}
