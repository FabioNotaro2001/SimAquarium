package env.model;

import java.util.Date;

public class DomainEvent {
    private Date time;
    private String eventDescription;


    public DomainEvent(Date time, String description){
        this.time = time;
        this.eventDescription = description;
    }

    public static DomainEvent of(String description){
        return new DomainEvent(new Date(), description);
    }

    public Date getTime(){
        return this.time;
    }

    public String getDescription(){
        return this.eventDescription;
    }
}
