package com.projectmanagement.lib.storage;

import java.util.List;

/**
 * @interface IRepository
 * @brief Generic repository interface for CRUD operations.
 * @tparam T The type of entity the repository manages.
 * @details Applies the Abstraction principle. Specifies standard behaviors for all storage types.
 */
public interface IRepository<T> {
    
    /**
     * @brief Creates a new entity in the repository.
     * @param entity The entity object to be saved.
     */
    void create(T entity);

    /**
     * @brief Retrieves an entity by its unique identifier.
     * @param id The unique identifier of the entity.
     * @return The entity object if found, null otherwise.
     */
    T findById(String id);

    /**
     * @brief Retrieves all entities from the repository.
     * @return A list containing all the entities.
     */
    List<T> findAll();

    /**
     * @brief Updates an existing entity in the repository.
     * @param entity The entity object containing the updated information.
     */
    void update(T entity);

    /**
     * @brief Deletes an entity from the repository based on its unique identifier.
     * @param id The unique identifier of the entity to be deleted.
     */
    void delete(String id);
}
