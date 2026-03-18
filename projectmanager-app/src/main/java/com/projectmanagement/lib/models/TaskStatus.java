package com.projectmanagement.lib.models;

/**
 * Represents the current status of a task.
 */
public enum TaskStatus {
    /**
     * Indicates that the task has not been started yet.
     */
    TODO,

    /**
     * Indicates that the task is currently being worked on.
     */
    IN_PROGRESS,

    /**
     * Indicates that the task has been completed.
     */
    DONE
}
