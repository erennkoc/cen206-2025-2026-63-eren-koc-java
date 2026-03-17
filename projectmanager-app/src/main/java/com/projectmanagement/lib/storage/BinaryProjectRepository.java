package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Project;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @class BinaryProjectRepository
 * @brief Binary File I/O implementation of the Project repository.
 * @details Implements IRepository to persist Project entities to local binary files.
 */
public class BinaryProjectRepository implements IRepository<Project> {

    /**
     * @brief The file path where project data is serialized.
     */
    private final String FILE_PATH = "projects.dat";

    /**
     * @brief Loads project list from the binary file.
     * @return A list of projects.
     */
    @SuppressWarnings("unchecked")
    private List<Project> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Project>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * @brief Saves the given project list to the binary file.
     * @param projects The list of projects to be saved.
     */
    private void saveToFile(List<Project> projects) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(projects);
        } catch (IOException e) {
            System.err.println("Error writing binary file: " + e.getMessage());
        }
    }

    /**
     * @brief Writes a new Project entity into the binary file.
     * @param entity The Project instance to log.
     */
    @Override
    public void create(Project entity) {
        List<Project> projects = loadFromFile();
        projects.add(entity);
        saveToFile(projects);
    }

    /**
     * @brief Reads a Project by ID from the binary file stream.
     * @param id The sought-after Project ID.
     * @return The Project object, or null on failure.
     */
    @Override
    public Project findById(String id) {
        List<Project> projects = loadFromFile();
        for (Project p : projects) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * @brief Loads all stored Project entries from binary serialization.
     * @return Complete list of documented Projects.
     */
    @Override
    public List<Project> findAll() {
        return loadFromFile();
    }

    /**
     * @brief Replaces an existing Project record in the binary file with new details.
     * @param entity Modified Project object.
     */
    @Override
    public void update(Project entity) {
        List<Project> projects = loadFromFile();
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getId().equals(entity.getId())) {
                projects.set(i, entity);
                break;
            }
        }
        saveToFile(projects);
    }

    /**
     * @brief Eliminates a Project by ID from the binary data store.
     * @param id Distinct identifier of the target Project.
     */
    @Override
    public void delete(String id) {
        List<Project> projects = loadFromFile();
        projects.removeIf(p -> p.getId().equals(id));
        saveToFile(projects);
    }
}
