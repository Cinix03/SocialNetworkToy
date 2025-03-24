package eu.example.src.controller;

import eu.example.src.domain.Friendship;
import eu.example.src.domain.Utilizator;
import eu.example.src.services.FriendshipService;
import eu.example.src.services.MessagesService;
import eu.example.src.services.UtilizatorService;
import eu.example.src.ui.ErrorPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView; // Asigură-te că importi ListView din JavaFX
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserHomePageController {

    private UtilizatorService utilizatorService;
    private FriendshipService friendshipService;
    private MessagesService messagesService;
    private Utilizator utilizator;

    @FXML
    private Label numeUser;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> suggestionsListView;

    @FXML
    public void initialize() {
        searchField.setMaxWidth(400); // Setăm o lățime maximă pentru câmpul de căutare
        suggestionsListView.setMaxWidth(400);
        // Setăm un listener pentru căutare
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                // Conversie Iterable la List
                List<Utilizator> utilizatoriList = new ArrayList<>();
                for (Object u : utilizatorService.findAll()) {
                    utilizatoriList.add((Utilizator) u);
                }

                // Filtrăm utilizatorii care conțin textul introdus
                List<String> filteredUsers = utilizatoriList.stream()
                        .filter(user -> user.getUsername().toLowerCase().contains(newValue.toLowerCase()))
                        .map(Utilizator::getUsername)
                        .collect(Collectors.toList());

                // Actualizăm ListView-ul cu sugestiile
                suggestionsListView.getItems().setAll(filteredUsers);
                suggestionsListView.setVisible(true); // Afișăm lista
            } else {
                suggestionsListView.getItems().clear();
                suggestionsListView.setVisible(false); // Ascundem lista dacă nu sunt sugestii
            }
        });

        // Ascundem ListView dacă nu sunt sugestii
        suggestionsListView.setVisible(false);

        suggestionsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                searchField.setText(newValue);
                suggestionsListView.setVisible(false);
            }
        });
    }


    public UserHomePageController() {
    }

    public void setUtilizatorService(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
        friendshipService.addSpecificChangeListener(this::sendNotificationFriendRequest);
    }

    public void sendNotificationFriendRequest(Long idReceiver, String status)
    {
        if(idReceiver==utilizator.getId() && status.equals("pending"))
            ErrorPopup.showInformation("Notificare", "Ai primit o cerere de prietenie!");
    }

    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }


    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
        System.out.println(utilizator);
        numeUser.setText("Welcome " + utilizator.getFirstName() + " " + utilizator.getLastName() + "!");
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        try {
            // Încarcă fereastra de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/example/fxml/login.fxml"));
            Parent loginRoot = loader.load();

            // Creează o scenă pentru fereastra de login
            Stage stage = (Stage) numeUser.getScene().getWindow();
            stage.setScene(new Scene(loginRoot, 1000, 1000));
            stage.show();

            // Setează controller-ul pentru pagina de login
            LoginController loginController = loader.getController();
            loginController.setUtilizatorService(utilizatorService);
            loginController.setFriendshipService(friendshipService);

        } catch (Exception e) {
            ErrorPopup.showError("Eroare", "Nu s-a putut încărca pagina de login: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddFriend() {
        String friendUsername = searchField.getText();
        try {
            Utilizator friend = utilizatorService.findByUsername(friendUsername);
            Friendship friendship = new Friendship(utilizator.getId(), friend.getId());
            friendship.setStatus("pending");
            friendship.setSender(utilizator.getId());
            friendship.setReceiver(friend.getId());
            friendshipService.add(friendship);
            ErrorPopup.showInformation("Succes", "Utilizatorului " + friendUsername + " i-a fost trimisa o cerere de prietenie.");
            searchField.setText("");
        }catch (Exception e){
            ErrorPopup.showError("Eroare", e.getMessage());
        }

    }

    @FXML
    public void handleFriends(){
        try {
            URL fxmlLocation = getClass().getResource("/eu/example/fxml/friends.fxml");
            if(fxmlLocation == null)
            {
                System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            FriendsController friendsController = loader.getController();
            friendsController.setUtilizatorService(utilizatorService);
            friendsController.setId(utilizator.getId());
            friendsController.setFriendshipService(friendshipService);
            System.out.println(messagesService);
            friendsController.setMessagesService(messagesService);

            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 1000));

        } catch (Exception e) {
            ErrorPopup.showError("Eroare", e.getMessage());
        }
    }

    @FXML
    public void handleProfile(){
        try {
            URL fxmlLocation = getClass().getResource("/eu/example/fxml/profile.fxml");
            if(fxmlLocation == null)
            {
                System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setUtilizatorService(utilizatorService);
            profileController.setUtilizator(utilizator);
            profileController.setFriendshipService(friendshipService);
            profileController.setMessagesService(messagesService);

            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 1000));

        } catch (Exception e) {
            ErrorPopup.showError("Eroare", e.getMessage());
        }
    }
    @FXML
    public void handleShowProfile(){
        try {
            URL fxmlLocation = getClass().getResource("/eu/example/fxml/otherprofile.fxml");
            if(fxmlLocation == null)
            {
                System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            String usr = searchField.getText();
            Utilizator cautat = utilizatorService.findByUsername(usr);
            System.out.println("Cautatul are: " + cautat.getId());
            System.out.println("Actualul are: " + cautat.getUsername());

            OtherProfileController profileController = loader.getController();
            profileController.setUtilizatorCurent(cautat);
            profileController.setFriendshipService(friendshipService);
            profileController.setUtilizatorService(utilizatorService);
            profileController.setUtilizator(utilizator);
            profileController.setMessagesService(messagesService);

            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 1000));

        } catch (Exception e) {
            ErrorPopup.showError("Eroare", e.getMessage());
            System.out.println(e.getMessage());

        }
    }
}
