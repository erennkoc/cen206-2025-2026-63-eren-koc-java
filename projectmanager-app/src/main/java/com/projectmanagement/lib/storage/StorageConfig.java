package com.projectmanagement.lib.storage;

/**
 * @class StorageConfig
 * @brief Manages the application's current active storage type configuration.
 * @details Configures whether the application will use Binary file, SQLite, or MySQL.
 */
public class StorageConfig {
    
    /**
     * @brief The current storage backend type.
     */
    private StorageType activeStorageType;

    /**
     * @brief Constructor for the storage configuration.
     * @param defaultStorageThe The initial storage backend type to use.
     */
    public StorageConfig(StorageType defaultStorage) {
        this.activeStorageType = defaultStorage;
    }

    /**
     * @brief Gets the active storage backend type.
     * @return The active StorageType.
     */
    public StorageType getActiveStorageType() {
        return activeStorageType;
    }

    /**
     * @brief Sets the active storage backend type.
     * @param activeStorageType The new StorageType to set.
     */
    public void setActiveStorageType(StorageType activeStorageType) {
        this.activeStorageType = activeStorageType;
    }
}
