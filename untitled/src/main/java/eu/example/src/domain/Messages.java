package eu.example.src.domain;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Messages extends Entity<Long>{
    private Long from;
    private Long to;
    private String message;
    private Timestamp date;

    public Messages(Long from, Long to, String message, Timestamp date) {
        super();
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
    }


    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long To) {
        this.to = To;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
