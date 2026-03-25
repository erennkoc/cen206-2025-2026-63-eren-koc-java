package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.User;

/**
 * Factory class to resolve and retrieve specific storage repositories.
 * <p> Examines the current StorageConfig and supplies the correct implementation (Binary, SQLite, MySQL) for User, Project, or Task. Applies the Factory Design Pattern.
 */
public class RepositoryFactory {

    /**
     * The configuration dictating which storage type to manufacture.
     */
    private StorageConfig config;

    /**
     * Constructor for the RepositoryFactory.
     * @param config The active storage configuration to reference during instantiation.
     */
    public RepositoryFactory(StorageConfig config) {
        this.config = config;
    }

    /**
     * Generates the matched IRepository for User domains.
     * @return The correctly typed User repository.
     */
    public IRepository<User> getUserRepository() {
        if (config.getActiveStorageType() == StorageType.BINARY_FILE) {
            return new BinaryUserRepository();
        } else if (config.getActiveStorageType() == StorageType.SQLITE) {
            return new SQLiteUserRepository();
        } else if (config.getActiveStorageType() == StorageType.MYSQL) {
            return new MySQLUserRepository();
        }
        throw new IllegalStateException("Unsupported storage type for Users");
    }

    /**
     * Generates the matched IRepository for Project domains.
     * @return The correctly typed Project repository.
     */
    public IRepository<Project> getProjectRepository() {
        if (config.getActiveStorageType() == StorageType.BINARY_FILE) {
            return new BinaryProjectRepository();
        } else if (config.getActiveStorageType() == StorageType.SQLITE) {
            return new SQLiteProjectRepository();
        } else if (config.getActiveStorageType() == StorageType.MYSQL) {
            return new MySQLProjectRepository();
        }
        throw new IllegalStateException("Unsupported storage type for Projects");
    }

    /**
     * Generates the matched IRepository for Task domains.
     * @return The correctly typed Task repository.
     */
    public IRepository<Task> getTaskRepository() {
        if (config.getActiveStorageType() == StorageType.BINARY_FILE) {
            return new BinaryTaskRepository();
        } else if (config.getActiveStorageType() == StorageType.SQLITE) {
            return new SQLiteTaskRepository();
        } else if (config.getActiveStorageType() == StorageType.MYSQL) {
            return new MySQLTaskRepository();
        }
        throw new IllegalStateException("Unsupported storage type for Tasks");
    }
}
