package com.example.ex_motricite;

/**
 * The {@code Actor} class represents a generic actor with basic information such as
 * ID, name, and first name.
 * <p>
 * This class serves as an abstract base class for specific types of actors in the system.
 * </p>
 *
 * @author Rgouaud
 * @version 1.0
 */
public abstract class Actor {

    /**
     * The unique identifier for the actor.
     */
    protected long id;

    /**
     * The last name of the actor.
     */
    protected String name;

    /**
     * The first name of the actor.
     */
    protected String firstName;

    /**
     * Constructs a new {@code Actor} with the specified ID, name, and first name.
     *
     * @param id         The unique identifier for the actor.
     * @param name       The last name of the actor.
     * @param firstName  The first name of the actor.
     */
    protected Actor(long id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the actor.
     *
     * @return The last name of the actor.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the first name of the actor.
     *
     * @return The first name of the actor.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Gets the unique identifier for the actor.
     *
     * @return The unique identifier for the actor.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Sets the last name of the actor.
     *
     * @param name The new last name of the actor.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the first name of the actor.
     *
     * @param firstName The new first name of the actor.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the unique identifier for the actor.
     *
     * @param id The new unique identifier for the actor.
     */
    public void setId(long id) {
        this.id = id;
    }
}
