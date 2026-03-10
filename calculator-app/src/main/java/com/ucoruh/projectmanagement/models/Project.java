package com.ucoruh.projectmanagement.models;

/**
 * Represents a project in the system.
 *
 * File format (projects.txt) — one project per line:
 *   id|name|description|ownerUsername|createdAt
 *   e.g.  P001|Website Redesign|Redesign company site|john|2026-03-10
 *
 * IDs are generated as P + zero-padded sequential number (P001, P002 …).
 */
public class Project {

    // -------------------------------------------------------
    //  Fields
    // -------------------------------------------------------

    /** Auto-generated unique identifier (e.g. "P001"). */
    private String id;

    /** Human-readable project name. */
    private String name;

    /** Short description of the project's purpose. */
    private String description;

    /** Username of the person who created the project. */
    private String ownerUsername;

    /** Date the project was created (stored as yyyy-MM-dd string). */
    private String createdAt;

    // -------------------------------------------------------
    //  Constructors
    // -------------------------------------------------------

    /**
     * Full constructor used when creating a new project.
     *
     * @param id            unique identifier
     * @param name          project name
     * @param description   short description
     * @param ownerUsername creator's username
     * @param createdAt     creation date string (yyyy-MM-dd)
     */
    public Project(String id, String name, String description,
                   String ownerUsername, String createdAt) {
        this.id            = id;
        this.name          = name;
        this.description   = description;
        this.ownerUsername = ownerUsername;
        this.createdAt     = createdAt;
    }

    // -------------------------------------------------------
    //  Getters and Setters
    // -------------------------------------------------------

    public String getId()            { return id; }
    public void   setId(String id)   { this.id = id; }

    public String getName()                  { return name; }
    public void   setName(String name)       { this.name = name; }

    public String getDescription()               { return description; }
    public void   setDescription(String desc)    { this.description = desc; }

    public String getOwnerUsername()                     { return ownerUsername; }
    public void   setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public String getCreatedAt()                 { return createdAt; }
    public void   setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // -------------------------------------------------------
    //  Serialization / Deserialization helpers
    // -------------------------------------------------------

    /**
     * Converts this Project into a pipe-delimited string for file storage.
     * @return "id|name|description|ownerUsername|createdAt"
     */
    public String toFileString() {
        return id + "|" + name + "|" + description + "|" + ownerUsername + "|" + createdAt;
    }

    /**
     * Parses a pipe-delimited line from projects.txt back into a Project object.
     *
     * @param line a single line from the projects file
     * @return a Project object, or null if the line is malformed
     */
    public static Project fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.trim().split("\\|");
        if (parts.length != 5) return null;
        return new Project(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    // -------------------------------------------------------
    //  toString
    // -------------------------------------------------------

    @Override
    public String toString() {
        return "Project{id='" + id + "', name='" + name + "', owner='" + ownerUsername + "'}";
    }
}
