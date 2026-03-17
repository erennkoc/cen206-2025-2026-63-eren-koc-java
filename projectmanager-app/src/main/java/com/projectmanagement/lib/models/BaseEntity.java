package com.projectmanagement.lib.models;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @class BaseEntity
 * @brief Abstract base class for all domain models.
 * @details Applies the Abstraction and Inheritance principles of OOP. Provides common properties like id and createdDate for all entities.
 */
public abstract class BaseEntity implements Serializable {
    /**
     * @brief Serial version UID for serialization compatibility.
     */
    private static final long serialVersionUID = 1L;
    /**
     * @brief Unique identifier for the entity.
     */
    private String id;
    
    /**
     * @brief The date and time when the entity was created.
     */
    private LocalDateTime createdDate;

    /**
     * @brief Default constructor for BaseEntity.
     * @details Initializes the createdDate to the current date and time.
     */
    public BaseEntity() {
        this.createdDate = LocalDateTime.now();
    }
    
    /**
     * @brief Parameterized constructor for BaseEntity.
     * @param id The unique identifier for the entity.
     */
    public BaseEntity(String id) {
        this();
        this.id = id;
    }

    /**
     * @brief Gets the unique identifier of the entity.
     * @return The entity's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * @brief Sets the unique identifier of the entity.
     * @param id The new ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @brief Gets the creation date of the entity.
     * @return The date and time the entity was created.
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * @brief Sets the creation date of the entity.
     * @param createdDate The new creation date.
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @brief Abstract method to display entity details.
     * @details Demonstrates Polymorphism. Each subclass must implement this method to provide specific details.
     */
    public abstract void displayDetails();
}
