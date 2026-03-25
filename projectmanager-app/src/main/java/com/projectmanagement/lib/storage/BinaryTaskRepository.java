package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.Task;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Binary File I/O implementation of the Task repository.
 * <p> Implements IRepository to persist Task entities to local binary file storage.
 */
public class BinaryTaskRepository implements IRepository<Task> {

    /**
     * The file path where task data is serialized.
     */
    private final String FILE_PATH = "tasks.dat";

    /**
     * Loads task list from the binary file.
     * @return A list of tasks.
     */
    @SuppressWarnings("unchecked")
    private List<Task> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Task>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Saves the given task list to the binary file.
     * @param tasks The list of tasks to be saved.
     */
    private void saveToFile(List<Task> tasks) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.err.println("Error writing binary file: " + e.getMessage());
        }
    }

    /**
     * Generates a new Task recording in the binary file.
     * @param entity The Task form to log.
     */
    @Override
    public void save(Task entity) {
        List<Task> tasks = loadFromFile();
        tasks.add(entity);
        saveToFile(tasks);
    }

    /**
     * Fetches a Task by correlating ID from the binary data stream.
     * @param id The matching Task ID.
     * @return The target Task object, resolving to null otherwise.
     */
    @Override
    public Task findById(String id) {
        List<Task> tasks = loadFromFile();
        for (Task t : tasks) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Interprets all Task entries secured inside binary files.
     * @return Collection array of Tasks.
     */
    @Override
    public List<Task> findAll() {
        return loadFromFile();
    }

    /**
     * Refreshes an operational Task entity in binary file content.
     * @param entity Altered Task structure holding new details.
     */
    @Override
    public void update(Task entity) {
        List<Task> tasks = loadFromFile();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(entity.getId())) {
                tasks.set(i, entity);
                break;
            }
        }
        saveToFile(tasks);
    }

    /**
     * Strips a Task entity from the binary footprint by ID matching.
     * @param id The corresponding identifier.
     */
    @Override
    public void delete(String id) {
        List<Task> tasks = loadFromFile();
        tasks.removeIf(t -> t.getId().equals(id));
        saveToFile(tasks);
    }
}
