package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.User;

/**
 * @class RepositoryFactory
 * @brief Factory class to resolve and retrieve specific storage repositories.
 * @details Examines the current StorageConfig and supplies the correct implementation (Binary, SQLite, MySQL) for User, Project, or Task. Applies the Factory Design Pattern.
 */
public class RepositoryFactory {

    /**
     * @brief The configuration dictating which storage type to manufacture.
     */
    private StorageConfig config;

    /**
     * @brief Constructor for the RepositoryFactory.
     * @param config The active storage configuration to reference during instantiation.
     */
    public RepositoryFactory(StorageConfig config) {
        this.config = config;
    }

    /**
     * @brief Generates the matched IRepository for User domains.
     * @return The correctly typed User repository.
     * @throws IllegalStateException If the configured storage type is unsupported.
     */
    public IRepository<User> getUserRepository() {
        switch (config.getActiveStorageType()) {
            case BINARY_FILE:
                return new BinaryUserRepository();
            case SQLITE:
                return new SQLiteUserRepository();
            case MYSQL:
                return new MySQLUserRepository();
            default:
                throw new IllegalStateException("Unsupported storage type for Users");
        }
    }

    /**
     * @brief Generates the matched IRepository for Project domains.
     * @return The correctly typed Project repository.
     * @throws IllegalStateException If the configured storage type is unsupported.
     */
    public IRepository<Project> getProjectRepository() {
        switch (config.getActiveStorageType()) {
            case BINARY_FILE:
                return new BinaryProjectRepository();
            case SQLITE:
                return new SQLiteProjectRepository();
            case MYSQL:
                return new MySQLProjectRepository();
            default:
                throw new IllegalStateException("Unsupported storage type for Projects");
        }
    }

    /**
     * @brief Generates the matched IRepository for Task domains.
     * @return The correctly typed Task repository.
     * @throws IllegalStateException If the configured storage type is unsupported.
     */
    public IRepository<Task> getTaskRepository() {
        switch (config.getActiveStorageType()) {
            case BINARY_FILE:
                return new BinaryTaskRepository();
            case SQLITE:
                return new SQLiteTaskRepository();
            case MYSQL:
                return new MySQLTaskRepository();
            default:
                throw new IllegalStateException("Unsupported storage type for Tasks");
        }
    }
}
