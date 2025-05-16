package env.model;

import java.util.Date;

/**
 * The `DomainEvent` class represents an event that occurs within the simulation.
 * Each event is associated with a timestamp and a description.
 */
public class DomainEvent {
    private Date time;
    private String eventDescription;

    /**
     * Constructs a new `DomainEvent` with the specified time and description.
     * 
     * @param time The time when the event occurred.
     * @param description A description of the event.
     */
    public DomainEvent(Date time, String description){
        this.time = time;
        this.eventDescription = description;
    }

    /**
     * Creates a new `DomainEvent` with the current time and the specified description.
     * 
     * @param description A description of the event.
     * @return A new `DomainEvent` instance.
     */
    public static DomainEvent of(String description){
        return new DomainEvent(new Date(), description);
    }

    /**
     * Retrieves the time when the event occurred.
     * 
     * @return The time of the event.
     */
    public Date getTime(){
        return this.time;
    }

    /**
     * Retrieves the description of the event.
     * 
     * @return The event description.
     */
    public String getDescription(){
        return this.eventDescription;
    }
}
