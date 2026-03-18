package com.projectmanagement.lib.storage;

import com.projectmanagement.lib.models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Binary File I/O implementation of the User repository.
 * <p> Implements IRepository. Handles CRUD operations for User entities using a Binary File.
 */
public class BinaryUserRepository implements IRepository<User> {

    /**
     * The file path where user data is serialized.
     */
    private final String FILE_PATH = "users.dat";

    /**
     * Loads user list from the binary file.
     * @return A list of users.
     */
    @SuppressWarnings("unchecked")
    private List<User> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<User>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Saves the given user list to the binary file.
     * @param users The list of users to be saved.
     */
    private void saveToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error writing binary file: " + e.getMessage());
        }
    }

    /**
     * Creates a new User record in the binary file.
     * @param entity The User object to save.
     */
    @Override
    public void create(User entity) {
        List<User> users = loadFromFile();
        users.add(entity);
        saveToFile(users);
    }

    /**
     * Finds a User by corresponding ID.
     * @param id The unique identifier.
     * @return The matched User, or null if not found.
     */
    @Override
    public User findById(String id) {
        List<User> users = loadFromFile();
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Retrieves a roster of all Users from the binary file.
     * @return List of all stored User entities.
     */
    @Override
    public List<User> findAll() {
        return loadFromFile();
    }

    /**
     * Overwrites an existing User's data in the binary file.
     * @param entity The updated User instance containing changes.
     */
    @Override
    public void update(User entity) {
        List<User> users = loadFromFile();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(entity.getId())) {
                users.set(i, entity);
                break;
            }
        }
        saveToFile(users);
    }

    /**
     * Purges a User from the binary storage system by ID.
     * @param id The ID of the User to delete.
     */
    @Override
    public void delete(String id) {
        List<User> users = loadFromFile();
        users.removeIf(u -> u.getId().equals(id));
        saveToFile(users);
    }
}
