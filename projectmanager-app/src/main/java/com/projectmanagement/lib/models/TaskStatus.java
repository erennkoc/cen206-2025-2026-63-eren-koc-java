package com.projectmanagement.lib.models;

/**
 * @enum TaskStatus
 * @brief Represents the current status of a task.
 */
public enum TaskStatus {
    /**
     * @brief Indicates that the task has not been started yet.
     */
    TODO,

    /**
     * @brief Indicates that the task is currently being worked on.
     */
    IN_PROGRESS,

    /**
     * @brief Indicates that the task has been completed.
     */
    DONE
}
