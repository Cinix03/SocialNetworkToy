package eu.example.src.validators;

import eu.example.src.domain.Utilizator;
import eu.example.src.repository.file.UtilizatorRepository;

import java.util.Scanner;
import java.util.regex.Pattern;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws InvalidUserException {
        //TODO: implement method validate
        if(entity.getFirstName().equals(""))
            throw new InvalidUserException("Utilizatorul nu este valid");
        if(entity.getLastName().equals(""))
            throw new InvalidUserException("Utilizatorul nu este valid");
        String regex = "^[A-Za-z]+(?: [A-Za-z]+)*$";

        // Creăm un pattern din regex
        Pattern pattern = Pattern.compile(regex);
        // Citim input-ul de la utilizator
        Scanner scanner = new Scanner(System.in);
        if(!pattern.matcher(entity.getFirstName()).matches())
            throw new InvalidUserException("Numele nu este valid");
        if(!pattern.matcher(entity.getLastName()).matches())
                throw new InvalidUserException("Prenumele nu este valid");
    }

}
