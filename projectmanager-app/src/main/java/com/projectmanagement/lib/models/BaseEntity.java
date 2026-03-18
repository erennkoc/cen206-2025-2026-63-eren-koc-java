package com.projectmanagement.lib.models;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Abstract base class for all domain models.
 * <p> Applies the Abstraction and Inheritance principles of OOP. Provides common properties like id and createdDate for all entities.
 */
public abstract class BaseEntity implements Serializable {
    /**
     * Serial version UID for serialization compatibility.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Unique identifier for the entity.
     */
    private String id;
    
    /**
     * The date and time when the entity was created.
     */
    private LocalDateTime createdDate;

    /**
     * Default constructor for BaseEntity.
     * <p> Initializes the createdDate to the current date and time.
     */
    public BaseEntity() {
        this.createdDate = LocalDateTime.now();
    }
    
    /**
     * Parameterized constructor for BaseEntity.
     * @param id The unique identifier for the entity.
     */
    public BaseEntity(String id) {
        this();
        this.id = id;
    }

    /**
     * Gets the unique identifier of the entity.
     * @return The entity's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the entity.
     * @param id The new ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the creation date of the entity.
     * @return The date and time the entity was created.
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the creation date of the entity.
     * @param createdDate The new creation date.
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Abstract method to display entity details.
     * <p> Demonstrates Polymorphism. Each subclass must implement this method to provide specific details.
     */
    public abstract void displayDetails();
}
