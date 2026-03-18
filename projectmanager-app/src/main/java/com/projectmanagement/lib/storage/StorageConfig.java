package com.projectmanagement.lib.storage;

/**
 * Manages the application's current active storage type configuration.
 * <p> Configures whether the application will use Binary file, SQLite, or MySQL.
 */
public class StorageConfig {
    
    /**
     * The current storage backend type.
     */
    private StorageType activeStorageType;

    /**
     * Constructor for the storage configuration.
     * @param defaultStorage The initial storage backend type to use.
     */
    public StorageConfig(StorageType defaultStorage) {
        this.activeStorageType = defaultStorage;
    }

    /**
     * Gets the active storage backend type.
     * @return The active StorageType.
     */
    public StorageType getActiveStorageType() {
        return activeStorageType;
    }

    /**
     * Sets the active storage backend type.
     * @param activeStorageType The new StorageType to set.
     */
    public void setActiveStorageType(StorageType activeStorageType) {
        this.activeStorageType = activeStorageType;
    }
}
