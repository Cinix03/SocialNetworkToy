package eu.example.src;

import eu.example.src.domain.Utilizator;
import eu.example.src.domain.Friendship;
import eu.example.src.domain.Tuple;
import eu.example.src.repository.Repository;
import eu.example.src.repository.database.FriendshipDatabaseRepo;
import eu.example.src.repository.database.UtilizatorDatabaseRepo;
import eu.example.src.repository.file.FriendshipRepository;
import eu.example.src.repository.file.UtilizatorRepository;
import eu.example.src.services.UtilizatorService;
import eu.example.src.services.FriendshipService;
//import eu.example.src.ui.Console;
import eu.example.src.ui.MainGraphicInterface;
import eu.example.src.validators.UtilizatorValidator;
import eu.example.src.validators.FriendshipValidator;


public class Main {
    public static void main(String[] args) {
        MainGraphicInterface.main(args);
        /*// Inițializăm validatoarele
        UtilizatorValidator utilizatorValidator = new UtilizatorValidator();

        //Inițializăm repository-urile
//        UtilizatorRepository repoFile = new UtilizatorRepository(utilizatorValidator, "./data/utilizatori.txt");
//        repoFile.initialize();
        UtilizatorDatabaseRepo repoUtilizator = new UtilizatorDatabaseRepo(utilizatorValidator,"jdbc:postgresql://localhost:5432/socialnetwork", "vasilegeorge", "parola");

        // Inițializăm validatorul de prietenie, având deja repo-ul utilizatorilor inițializat
        FriendshipValidator friendshipValidator = new FriendshipValidator(repoUtilizator);

        // Inițializăm repository-ul de prietenie
        FriendshipDatabaseRepo friendshipRepo = new FriendshipDatabaseRepo(friendshipValidator, "jdbc:postgresql://localhost:5432/socialnetwork", "vasilegeorge", "parola");

        // Creăm serviciul utilizator
        UtilizatorService utilizatorService = new UtilizatorService(repoUtilizator, utilizatorValidator);

        // Creăm serviciul pentru prietenie
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, friendshipValidator);

        // Creăm instanța consolei și rulăm aplicația
        Console console = new Console(utilizatorService, friendshipService);
        console.run();*/
    }
}
