package com.projectmanagement.lib.storage;

/**
 * @enum StorageType
 * @brief Enum representing the supported storage backend types.
 */
public enum StorageType {
    /**
     * @brief Indicates that data will be stored using Binary File I/O.
     */
    BINARY_FILE,

    /**
     * @brief Indicates that data will be stored using an SQLite database.
     */
    SQLITE,

    /**
     * @brief Indicates that data will be stored using a MySQL database.
     */
    MYSQL
}
