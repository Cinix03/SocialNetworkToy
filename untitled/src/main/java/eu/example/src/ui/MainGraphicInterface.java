package eu.example.src.ui;

import eu.example.src.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import eu.example.src.repository.database.UtilizatorDatabaseRepo;
import eu.example.src.services.UtilizatorService;
import eu.example.src.validators.UtilizatorValidator;
import eu.example.src.repository.database.FriendshipDatabaseRepo;
import eu.example.src.services.FriendshipService;
import eu.example.src.validators.FriendshipValidator;

import java.awt.*;
import java.io.File;
import java.net.URL;


public class MainGraphicInterface extends Application{

    private UtilizatorService utilizatorService;
    private FriendshipService friendshipService;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("start");

        UtilizatorValidator utilizatorValidator = new UtilizatorValidator();
        UtilizatorDatabaseRepo repoUtilizator = new UtilizatorDatabaseRepo(utilizatorValidator, "jdbc:postgresql://localhost:5432/socialnetwork", "vasilegeorge", "parola");
        FriendshipValidator friendshipValidator = new FriendshipValidator(repoUtilizator);
        FriendshipDatabaseRepo friendshipRepo = new FriendshipDatabaseRepo(friendshipValidator, "jdbc:postgresql://localhost:5432/socialnetwork", "vasilegeorge", "parola");

        utilizatorService = new UtilizatorService(repoUtilizator, utilizatorValidator);
        friendshipService = new FriendshipService(friendshipRepo, friendshipValidator);

        // Corectarea locației FXML
        URL fxmlLocation = getClass().getResource("/eu/example/fxml/login.fxml");
        if (fxmlLocation == null) {
            System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        LoginController loginController = loader.getController();
        loginController.setUtilizatorService(utilizatorService);
        loginController.setFriendshipService(friendshipService);

        stage.setTitle("Social Network");
        Scene scene = new Scene(root, 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }


    public void init() {
                // Inițializăm validatoarele
        UtilizatorValidator utilizatorValidator = new UtilizatorValidator();

        // Inițializăm repository-urile
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

    }

    public static void main(String[] args) {
        launch(args);
    }
}
