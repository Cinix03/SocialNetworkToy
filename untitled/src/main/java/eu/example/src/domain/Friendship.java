package eu.example.src.domain;

import java.time.LocalDateTime;
import java.util.Objects;


public class Friendship extends Entity<Tuple<Long, Long>> {
    private LocalDateTime date;
    private String status;
    private Long sender;
    private Long receiver;

    public Friendship(Long id1, Long id2) {
        this.setId(new Tuple<>(id1, id2));
        this.date = LocalDateTime.now();
    }

    public String getStatus(){
        return status;
    }

    public Long getSender(){
        return sender;
    }

    public Long getReceiver(){
        return receiver;
    }

    public void setSender(Long i){
        this.sender = i;
    }

    public void setReceiver(Long i){
        this.receiver = i;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setStatus(String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "users=" + getId() +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
