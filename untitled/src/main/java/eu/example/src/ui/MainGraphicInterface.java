package eu.example.src.ui;

import eu.example.src.controller.LoginController;
import eu.example.src.repository.database.MessagesDatabaseRepo;
import eu.example.src.services.MessagesService;
import eu.example.src.validators.MessagesValidator;
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


public class MainGraphicInterface extends Application {
    private UtilizatorService utilizatorService;
    private FriendshipService friendshipService;
    private MessagesService messagesService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inițializarea serviciilor
        UtilizatorValidator utilizatorValidator = new UtilizatorValidator();
        UtilizatorDatabaseRepo repoUtilizator = new UtilizatorDatabaseRepo(utilizatorValidator, "jdbc:postgresql://localhost:5432/socialnetwork", "vasilegeorge", "parola");
        FriendshipValidator friendshipValidator = new FriendshipValidator(repoUtilizator);
        FriendshipDatabaseRepo friendshipRepo = new FriendshipDatabaseRepo(friendshipValidator, "jdbc:postgresql://localhost:5432/socialnetwork", "vasilegeorge", "parola");
        MessagesValidator messagesValidator = new MessagesValidator();
        MessagesDatabaseRepo messagesRepo = new MessagesDatabaseRepo(messagesValidator,"jdbc:postgresql://localhost:5432/socialnetwork", "vasilegeorge", "parola");


        utilizatorService = new UtilizatorService(repoUtilizator, utilizatorValidator);
        friendshipService = new FriendshipService(friendshipRepo, friendshipValidator);
        messagesService = new MessagesService(messagesRepo, messagesValidator);

        // Crează prima fereastră de login
        createLoginWindow();

        // Crează a doua fereastră de login (pentru a testa deschiderea multiplă)
        createLoginWindow();
    }

    private void createLoginWindow() throws Exception {
        URL fxmlLocation = getClass().getResource("/eu/example/fxml/login.fxml");
        if (fxmlLocation == null) {
            System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        // Obține controllerul și setează serviciile
        LoginController loginController = loader.getController();
        loginController.setUtilizatorService(utilizatorService);
        loginController.setFriendshipService(friendshipService);
        loginController.setMessagesService(messagesService);

        // Creează o nouă fereastră (Stage)
        Stage stage = new Stage();
        stage.setTitle("Social Network - Login");
        stage.setScene(new Scene(root, 1000, 1000));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
