package com.ucoruh.projectmanagement.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @class FileHelper
 * @brief Utility class for reading and writing serialized object lists.
 * @details Provides generic save/load methods so each service only needs to
 *          call a single method to persist or retrieve its data. Data is stored
 *          as serialized Java objects in binary files under the "data/" folder.
 */
public class FileHelper {

    /**
     * @brief Saves a list of serializable objects to a file.
     *
     * @param <T>      type of objects in the list
     * @param filePath path to the target file (created if it does not exist)
     * @param list     the list to persist
     */
    public static <T> void saveList(String filePath, List<T> list) {
        // Ensure parent directories exist
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save data to " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * @brief Loads a list of serializable objects from a file.
     *
     * @param <T>      type of objects in the list
     * @param filePath path to the source file
     * @return the deserialized list, or an empty list if the file is missing or corrupt
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadList(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Could not load data from " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
