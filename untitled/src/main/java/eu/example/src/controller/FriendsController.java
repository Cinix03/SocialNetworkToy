package eu.example.src.controller;

import eu.example.src.domain.Friendship;
import eu.example.src.domain.Tuple;
import eu.example.src.domain.Utilizator;
import eu.example.src.services.FriendshipService;
import eu.example.src.services.UtilizatorService;

import eu.example.src.ui.ErrorPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


import java.util.Iterator;
import java.util.Optional;

public class FriendsController {

    private String currentView = "friends";  // Starea curentă a paginii (friends, requestsReceived, requestsSent)
    public Label Prietenii;
    public Button acceptButton;
    public Button declineButton;
    public Button blockButton;
    public Button RemoveFriend;
    private FriendshipService friendshipService;
    private UtilizatorService utilizatorService;
    private Long id;

    @FXML
    private Button backButton;

    @FXML
    private ListView<String> friendsList;

    public void setupController(){
        initialize();
    }

    @FXML
    public void initialize(){



    }

    public void setUtilizatorService(UtilizatorService UtilizatorService) {
        this.utilizatorService = UtilizatorService;
    }

    public void setFriendshipService(FriendshipService friends) {
        this.friendshipService = friends;
        friendshipService.addChangeListener(this::initModel);
        System.out.println("Am ajuns aici");
        initModel();
    }

    private void selectFriendsRequestSent(String stat){
        friendsList.getItems().clear();
        Iterable<Friendship> friendss = this.friendshipService.findAll();
        System.out.println(id);
        Iterator<Friendship> it = friendss.iterator();
        System.out.println("Am ajuns aici");
        while (it.hasNext()) {
            System.out.println("Am ajuns aici");
            Friendship f = it.next();
            if (f.getSender().equals(id) && f.getStatus().equals(stat)) {
                Optional<Utilizator> u = utilizatorService.findOne(f.getReceiver());
                u.ifPresent(value ->{
                    String ziLunaAnOra = f.getDate().getDayOfMonth() + "/" + f.getDate().getMonthValue() + "/" + f.getDate().getYear() + " " + f.getDate().getHour() + ":" + f.getDate().getMinute();
                    friendsList.getItems().add(value.getUsername().toString() + " | " + value.getFirstName().toString() + " " + value.getLastName().toString()+" | " + ziLunaAnOra);
                });
            }
        }

        friendsList.refresh();
    }


    private void selectFriendsRequestReceived(String stat){
        friendsList.getItems().clear();
        Iterable<Friendship> friendss = this.friendshipService.findAll();
        System.out.println(id);
        Iterator<Friendship> it = friendss.iterator();
        System.out.println("Am ajuns aici");
        while (it.hasNext()) {
            System.out.println("Am ajuns aici");
            Friendship f = it.next();
            System.out.println(f.getId());
            if (f.getReceiver().equals(id) && f.getStatus().equals(stat)) {
                System.out.println(f.getId().getFirst());
                Optional<Utilizator> u = utilizatorService.findOne(f.getSender());
                u.ifPresent(value ->{
                    String ziLunaAnOra = f.getDate().getDayOfMonth() + "/" + f.getDate().getMonthValue() + "/" + f.getDate().getYear() + " " + f.getDate().getHour() + ":" + f.getDate().getMinute();
                    friendsList.getItems().add(value.getUsername().toString() + " | " + value.getFirstName().toString() + " " + value.getLastName().toString()+" | " + ziLunaAnOra);
                });
            }
        }

        friendsList.refresh();
    }

    @FXML
    private void initModel() {
        acceptButton.setVisible(false);
        declineButton.setVisible(false);
        blockButton.setVisible(false);
        friendsList.getItems().clear();

        // Verificăm valoarea variabilei currentView pentru a apela metoda potrivită
        switch (currentView) {
            case "friends":
                loadFriendsList();
                break;
            case "requestsReceived":
                acceptButton.setVisible(true);
                declineButton.setVisible(true);
                blockButton.setVisible(true);
                selectFriendsRequestReceived("pending");
                break;
            case "requestsSent":
                selectFriendsRequestSent("pending");
                break;
        }
    }

    private void loadFriendsList() {
        Iterable<Friendship> friendss = this.friendshipService.findAll();
        System.out.println(id);
        Iterator<Friendship> it = friendss.iterator();
        System.out.println("Am ajuns aici");

        while (it.hasNext()) {
            System.out.println("Am ajuns aici");
            Friendship f = it.next();
            if (f.getId().getFirst().equals(id) && f.getStatus().equals("accepted")) {
                System.out.println(f.getId().getSecond());
                Optional<Utilizator> u = utilizatorService.findOne(f.getId().getSecond());
                u.ifPresent(value -> {
                    friendsList.getItems().add(value.getUsername() + " | " + value.getFirstName() + " " + value.getLastName() + " | " + f.getDate().toString());
                });
            } else if (f.getId().getSecond().equals(id) && f.getStatus().equals("accepted")) {
                System.out.println(f.getId().getFirst());
                Optional<Utilizator> u = utilizatorService.findOne(f.getId().getFirst());
                u.ifPresent(value -> {
                    String ziLunaAnOra = f.getDate().getDayOfMonth() + "/" + f.getDate().getMonthValue() + "/" + f.getDate().getYear() + " " + f.getDate().getHour() + ":" + f.getDate().getMinute();
                    friendsList.getItems().add(value.getUsername() + " | " + value.getFirstName() + " " + value.getLastName() + " | " + ziLunaAnOra);
                });
            }
        }
        friendsList.refresh();
    }

    @FXML
    public void handleFriendshipRequests() {
        currentView = "requestsReceived";  // Setează starea la cereri primite
        acceptButton.setVisible(true);
        declineButton.setVisible(true);
        blockButton.setVisible(true);
        selectFriendsRequestReceived("pending");
    }

    @FXML
    public void handleRequestsSent() {
        currentView = "requestsSent";  // Setează starea la cereri trimise
        acceptButton.setVisible(false);
        declineButton.setVisible(false);
        blockButton.setVisible(false);
        selectFriendsRequestSent("pending");
    }

    @FXML
    public void handleBackToFriends() {
        friendsList.getItems().clear();
        currentView = "friends";  // Setează starea la lista de prieteni
        acceptButton.setVisible(false);
        declineButton.setVisible(false);
        blockButton.setVisible(false);
        loadFriendsList();
    }

    @FXML
    public void handleBack(){
        //user page controller
        System.out.println("Back");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/example/fxml/userHomePage.fxml"));
            System.out.println("Back");
            Parent root = loader.load();
            UserHomePageController controller = loader.getController();
            controller.setUtilizatorService(utilizatorService);
            controller.setFriendshipService(friendshipService);
            controller.setUtilizator(utilizatorService.findOne(id).get());

            System.out.println("Back");
            Stage stage = (Stage) friendsList.getScene().getWindow();
            System.out.println("Back");
            stage.setScene(new Scene(root, 1000, 1000));
            System.out.println("Back");
        }catch (Exception e){
            ErrorPopup.showError("Eroare", "Nu s-a putut încărca pagina de login: " + e.getMessage());
        }
    }

    public void setId(Long id) {
        this.id = id;
    }
    @FXML
    public void handleBlockRequest() {
    }
    @FXML
    public void handleDeclineRequest() {
        String friendUsername = friendsList.getSelectionModel().getSelectedItem();
        String[] parts = friendUsername.split("\\|");
        String username = parts[0];
        username = username.trim();
        System.out.println(username);
        try {
            Utilizator friend = utilizatorService.findByUsername(username);
            Tuple<Long, Long> idF = new Tuple<>(id, friend.getId());
            if(idF.getFirst() > idF.getSecond()){
                Tuple<Long, Long> aux = new Tuple<>(idF.getSecond(), idF.getFirst());
                idF = aux;
            }
            friendshipService.findOne(idF).ifPresent(friendship -> {
                friendshipService.delete(new Friendship(id, friend.getId()));
            });
            ErrorPopup.showInformation("Succes", "Utilizatorul " + username + " a fost eliminat din lista de prieteni.");
            initModel();
        }catch (Exception e){
            ErrorPopup.showError("Eroare", e.getMessage());
        }
    }
@FXML
public void handleAcceptRequest() {
    String friendUsername = friendsList.getSelectionModel().getSelectedItem();
    String[] parts = friendUsername.split("\\|");
    String username = parts[0];
    username = username.trim();
    System.out.println(username);
    try {
        Utilizator friend = utilizatorService.findByUsername(username);
        Tuple<Long, Long> idF = new Tuple<>(id, friend.getId());
        if(idF.getFirst() > idF.getSecond()){
            Tuple<Long, Long> aux = new Tuple<>(idF.getSecond(), idF.getFirst());
            idF = aux;
        }
        friendshipService.findOne(idF).ifPresent(friendship -> {
            friendship.setStatus("accepted");
            friendshipService.delete(new Friendship(id, friend.getId()));
            friendshipService.add(friendship);
        });
        ErrorPopup.showInformation("Succes", "Utilizatorul " + username + " a fost adăugat la lista de prieteni.");
        initModel();
    }catch (Exception e){
        ErrorPopup.showError("Eroare", e.getMessage());
    }
}
    @FXML
    public void handleRemoveFriendship() {
        handleDeclineRequest();
    }
}
