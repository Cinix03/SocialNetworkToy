package eu.example.src.services;

import eu.example.src.domain.Friendship;
import eu.example.src.domain.Tuple;
import eu.example.src.domain.Utilizator;
import eu.example.src.repository.Repository;
import eu.example.src.repository.file.FriendshipRepository;
import eu.example.src.validators.Validator;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UtilizatorService extends AbstractService<Long, Utilizator> {
    public UtilizatorService(Repository<Long, Utilizator> repo, Validator<Utilizator> validator) {
        super(repo, validator);
    }


    public ArrayList<Utilizator> getFriends(Utilizator u){
        validator.validate(u);
        return u.getFriends();
    }


    @Override
    boolean isType(Object o) {
        if(o==null)
            return false;
        return o instanceof Utilizator;
    }

    @Override
    boolean isTypeOfID(Object o) {
        if(o==null)
            return false;
        return o instanceof Long;
    }
}
