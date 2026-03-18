package com.projectmanagement.lib.storage;

/**
 * Enum representing the supported storage backend types.
 */
public enum StorageType {
    /**
     * Indicates that data will be stored using Binary File I/O.
     */
    BINARY_FILE,

    /**
     * Indicates that data will be stored using an SQLite database.
     */
    SQLITE,

    /**
     * Indicates that data will be stored using a MySQL database.
     */
    MYSQL
}
