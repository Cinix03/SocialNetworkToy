package eu.example.src.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;

    private List<Utilizator> friends;

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<Utilizator> getFriends(){
        return new ArrayList<>(friends);
    }

    public void addFriend(Optional<Utilizator> u){
        if(u.isPresent()) {
            Utilizator u1 = u.get();
            friends.add(u1);
        }
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }

    public void removeFriend(Utilizator user2) {
        friends.remove(user2);
    }

    public void setId(Long idToDelete) {
        super.setId(idToDelete);
    }

    public Long getId() {
        return super.getId();
    }
}
