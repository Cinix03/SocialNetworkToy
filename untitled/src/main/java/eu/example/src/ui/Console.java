package eu.example.src.ui;

import eu.example.src.domain.Utilizator;
import eu.example.src.domain.*;
import eu.example.src.domain.Tuple;
import eu.example.src.services.UtilizatorService;
import eu.example.src.services.FriendshipService;
import eu.example.src.validators.*;

import java.util.Optional;
import java.util.Scanner;
import java.util.stream.StreamSupport;

public class Console {
    private UtilizatorService utilizatorService;
    private FriendshipService friendshipService;

    public Console(UtilizatorService utilizatorService, FriendshipService friendshipService) {
        this.utilizatorService = utilizatorService;
        this.friendshipService = friendshipService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println("Bine ai venit la aplicația de gestionare a utilizatorilor și prietenilor!");

        do {
            System.out.println("Introduceti comanda (add_user, delete_user, list_users, add_friendship, delete_friendship, list_friendships, how_many_com, most_sociable, friends_of_user, exit): ");
            command = scanner.nextLine();

            switch (command) {
                case "add_user":
                    System.out.println("Introduceti prenumele: ");
                    try {
                        String firstName = scanner.nextLine();
                        System.out.println("Introduceti numele de familie: ");
                        String lastName = scanner.nextLine();
                        Utilizator utilizator = new Utilizator(firstName, lastName);
                        utilizatorService.add(utilizator);
                        System.out.println("Utilizator adăugat cu succes!");
                    } catch (ValidationException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case "delete_user":
                    System.out.println("Introduceti ID-ul utilizatorului de sters: ");
                    try {
                        Long idToDelete = Long.parseLong(scanner.nextLine());
                        Utilizator utilizator = new Utilizator("fsafa", "Afsaf");
                        System.out.println(idToDelete);
                        utilizator.setId(idToDelete);
                        System.out.println(utilizator.getId());
                        utilizatorService.delete(utilizator);
                        System.out.println("Utilizator șters cu succes!");
                    } catch (ValidationException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case "list_users":
                    try {
                        System.out.println("Utilizatori:");
                        utilizatorService.findAll().forEach(utilizator -> {
                            Utilizator Utilizator1 = (Utilizator) utilizator;
                            System.out.println(Utilizator1);
                        });
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case "add_friendship":
                    System.out.println("Introduceti ID-ul utilizatorului 1: ");
                    try {
                        Long userId1 = Long.parseLong(scanner.nextLine());
                        System.out.println("Introduceti ID-ul utilizatorului 2: ");
                        Long userId2 = Long.parseLong(scanner.nextLine());

                        Friendship friendship = new Friendship(userId1, userId2);
                        friendshipService.add(friendship);
                        System.out.println("Prietenie adăugată cu succes!");
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "delete_friendship":
                    System.out.println("Introduceti ID-ul utilizatorului 1: ");
                    try {
                        Long delUserId1 = Long.parseLong(scanner.nextLine());
                        System.out.println("Introduceti ID-ul utilizatorului 2: ");
                        Long delUserId2 = Long.parseLong(scanner.nextLine());
                        Friendship friendshipTuple = new Friendship(delUserId1, delUserId2);
                        friendshipService.delete(friendshipTuple);
                        System.out.println("Prietenie ștearsă cu succes!");
                    } catch (ValidationException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;

                case "list_friendships":
                    System.out.println("Prietenii:");
                    try {
                        friendshipService.findAll().forEach(friendship -> {
                            System.out.println(friendship);
                        });
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case "how_many_com":
                    try {
                        System.out.println(friendshipService.nrComunitati((int) StreamSupport.stream(utilizatorService.findAll().spliterator(), false).count(), utilizatorService.findAll()));
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case "most_sociable":
                    try {
                        System.out.println(friendshipService.ceiMaiprietenosi((int) StreamSupport.stream(utilizatorService.findAll().spliterator(), false).count(), utilizatorService.findAll()));
                    }catch (Exception e){
                        System.out.println(e.getMessage() + "Eroare la service friendship/graph!");
                    }
                    break;

                case "friends_of_user":
                {
                    try {
                        System.out.println("Introduceti id ul userului: ");
                        Long userID = Long.parseLong(scanner.nextLine());
                        Optional<Utilizator> u1 = (Optional<Utilizator>) utilizatorService.findOne(userID);
                        if(!u1.isPresent())
                            throw new Exception("Nu exista utilizator cu acest id");
                        Utilizator u = u1.get();
                        Iterable<Friendship> friendships = friendshipService.findAll();
                        System.out.println("Prietenii utilizatorului " + u.getFirstName()+ " " + u.getLastName()+ ":");
                        friendships.forEach(friendship -> {
                            if(friendship.getId().getFirst() == u.getId())
                            {
                                Optional<Utilizator> u2 = (Optional<Utilizator>)utilizatorService.findOne(friendship.getId().getSecond());
                                if(u2.isPresent())
                                    System.out.println(u2.get().getFirstName() +" " + u2.get().getLastName());
                            }
                            else if(friendship.getId().getSecond() == u.getId())
                            {
                                Optional<Utilizator> u2 = (Optional<Utilizator>)utilizatorService.findOne(friendship.getId().getFirst());
                                if(u2.isPresent())
                                    System.out.println(u2.get().getFirstName() +" " + u2.get().getLastName());
                            }
                        });

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case "exit":
                    System.out.println("La revedere!");
                    break;

                default:
                    System.out.println("Comandă necunoscută. Vă rugăm să încercați din nou.");
            }
        } while (!command.equals("exit"));

        scanner.close();
    }
}
