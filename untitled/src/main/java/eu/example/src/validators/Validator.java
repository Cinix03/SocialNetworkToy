package eu.example.src.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
