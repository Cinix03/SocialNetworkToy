package eu.example.src.repository.file;

import eu.example.src.domain.Friendship;
import eu.example.src.domain.Entity;
import eu.example.src.domain.Tuple;
import eu.example.src.domain.Utilizator;
import eu.example.src.validators.InvalidFriendshipException;
import eu.example.src.validators.Validator;
import eu.example.src.repository.Repository;

import java.util.Map;
import java.util.Optional;

public class FriendshipRepository extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {
    private UtilizatorRepository userRepo; // Referința la UserRepository

    public FriendshipRepository(Validator<Friendship> validator,
                                String fileName,
                                UtilizatorRepository userR) {
        super(validator, fileName);
        this.userRepo = userR;
    }

    public void deleteUser(Long id){
        findAll().forEach(f -> {
            if (f.getId().getFirst().equals(id) || f.getId().getSecond().equals(id)) {
                delete(f.getId());
            }
        });
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> id){
        Optional<Friendship> res = super.delete(id);

        if(res.isPresent()){
            Friendship friendship = res.get();
            Optional<Utilizator> user1 = userRepo.findOne(friendship.getId().getFirst());
            Optional<Utilizator> user2 = userRepo.findOne(friendship.getId().getSecond());

            if(user1.isPresent() && user2.isPresent()){
                Utilizator user11 = user1.get();
                Utilizator user22 = user2.get();
                user11.removeFriend(user22);
                userRepo.update(user11);

                user22.removeFriend(user11);
                userRepo.update(user22);
            }
        }

        return res;
    }

    @Override
    public Friendship createEntity(String line) {

        // Sparge linia pe baza separatorului ";"
        String[] splited = line.split(";");

        // Parsează ID-urile utilizatorilor
        Long userId1 = Long.parseLong(splited[0]);
        Long userId2 = Long.parseLong(splited[1]);

        // Creează o prietenie cu aceste ID-uri
        Friendship friendship = new Friendship(userId1, userId2);

        // Caută utilizatorii în repository
        Optional<Utilizator> optionalUser1 = userRepo.findOne(userId1);
        Optional<Utilizator> optionalUser2 = userRepo.findOne(userId2);

        // Verifică dacă ambii utilizatori există
        if (optionalUser1.isPresent() && optionalUser2.isPresent()) {
            // Obține utilizatorii din Optional
            Utilizator user1 = optionalUser1.get();
            Utilizator user2 = optionalUser2.get();

            // Adaugă prietenii
            user1.addFriend(Optional.of(user2));
            userRepo.update(user1);

            user2.addFriend(Optional.of(user1));
            userRepo.update(user2);
        } else {
            // Aruncă o excepție dacă unul dintre utilizatori nu există
            throw new InvalidFriendshipException("Unul sau ambii utilizatori nu există.");
        }

        return friendship;
    }



    @Override
    public String saveEntity(Friendship entity) {
        return entity.getId().getFirst() + ";" + entity.getId().getSecond(); // Salvează prietenia în format text
    }

}
