package interfaces.validation;

import exception.InvalidInputException;

/**
 *
 * @author tancy
 */
public interface IValidator<T> {
    public boolean validate(T input) throws InvalidInputException;
}
