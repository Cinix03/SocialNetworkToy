package eu.example.src.validators;

public class InvalidUserException extends ValidationException{
    public InvalidUserException(String message){
        super(message);
    }
}
