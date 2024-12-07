package eu.example.src.validators;

import eu.example.src.domain.Messages;

public class MessagesValidator implements  Validator<Messages> {
    @Override
    public void validate(Messages entity) throws ValidationException {
        if(entity.getFrom() == null)
            throw new ValidationException("From-ul nu poate fi null");
        if(entity.getTo() == null)
            throw new ValidationException("To-ul nu poate fi null");
        if(entity.getMessage() == null)
            throw new ValidationException("Mesajul nu poate fi null");
    }
}
