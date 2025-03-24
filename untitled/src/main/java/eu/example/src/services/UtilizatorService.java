package eu.example.src.services;

import eu.example.src.domain.Utilizator;
import eu.example.src.repository.database.UtilizatorDatabaseRepo;
import eu.example.src.validators.Validator;

import java.util.ArrayList;
import java.util.Optional;

public class UtilizatorService extends AbstractService<Long, Utilizator> {
    public UtilizatorService(UtilizatorDatabaseRepo repo, Validator<Utilizator> validator) {
        super(repo, validator);
    }

    public Utilizator findByUsername(String username){
        for(Utilizator u:repo.findAll()){
            if(u.getUsername().equals(username))
                return u;
        }
        throw new IllegalArgumentException("Utilizatorul nu exista");
    }

    public Optional<Utilizator> findByUsernameDB(String username){
        return repo.findByUsernameDB(username);
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

    public void addImagePath(Utilizator u, String path){
        Utilizator u1 = repo.findOne(u.getId()).get();
        u1.setProfilePicturePath(path);
        repo.update(u1);
    }
}
