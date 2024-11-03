package eu.example.src.validators;

import eu.example.src.domain.Friendship;
import eu.example.src.domain.Utilizator;
import eu.example.src.repository.Repository;
import eu.example.src.repository.file.FriendshipRepository;
import eu.example.src.repository.file.UtilizatorRepository;

public class FriendshipValidator implements Validator<Friendship> {

    private Repository<Long, Utilizator> utilizatorRepository;
    public FriendshipValidator(Repository<Long, Utilizator> utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }
    @Override
    public void validate(Friendship entity) throws InvalidFriendshipException {
        if (entity.getId().getFirst() == null || entity.getId().getSecond() == null)
            throw new InvalidFriendshipException("Id-urile nu pot fi nule");
        if (entity.getId().getFirst().equals(entity.getId().getSecond()))
            throw new InvalidFriendshipException("Id-urile nu pot fi egale");
        if (utilizatorRepository.findOne(entity.getId().getFirst()) == null)
            throw new InvalidFriendshipException("Primul utilizator nu exista");
        if (utilizatorRepository.findOne(entity.getId().getSecond()) == null)
            throw new InvalidFriendshipException("Al doilea utilizator nu exista");
        if(entity.getId().getFirst() > entity.getId().getSecond()){
            Long aux = entity.getId().getFirst();
            entity.getId().setFirst(entity.getId().getSecond());
            entity.getId().setSecond(aux);
        }
        if(entity.getId().getFirst() == entity.getId().getSecond())
            throw new InvalidFriendshipException("Id-urile nu pot fi egale");
        if(!utilizatorRepository.findOne(entity.getId().getFirst()).isPresent() || !utilizatorRepository.findOne(entity.getId().getSecond()).isPresent())
            throw new InvalidFriendshipException("Nu exista unul sau ambii utilizatori");
    }
}
