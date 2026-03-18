package com.projectmanagement.lib.services;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.storage.IRepository;
import java.util.List;

/**
 * @class ProjectService
 * @brief Manages business operations and logic connected to Projects.
 * @details Responsible for core actions like creating a project and orchestrating modifications connected specifically to project data sets. Follows the Dependency Injection parameter layout.
 */
public class ProjectService {

    /**
     * @brief The Project repository used to interact with persistent storage.
     * @details Abstracted via IRepository to facilitate swapping between backend configurations seamlessly.
     */
    private final IRepository<Project> projectRepository;

    /**
     * @brief Instantiates the ProjectService utilizing the provided storage repository.
     * @param projectRepository The instantiated storage repository mechanism (dependency).
     */
    public ProjectService(IRepository<Project> projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * @brief Establishes a new Project and commits it to the designated storage backend.
     * @param id System-unique identifier for the Project.
     * @param name Name branding the Project.
     * @param description Full textual breakdown summarizing the Project's scope.
     * @throws IllegalArgumentException If core identifying attributes are omitted.
     */
    public void createProject(String id, String name, String description) {
        if (id == null || name == null || name.isEmpty() || description == null) {
            throw new IllegalArgumentException("Project initialization values cannot be void or blank.");
        }

        Project newProject = new Project(id, name, description);
        projectRepository.create(newProject);
    }

    /**
     * @brief Appends a Task to a persistent Project based on the Project's id.
     * @param projectId Valid identifier representing the target Project.
     * @param task System-ready Task object to attach to the Project.
     * @throws IllegalArgumentException If the given projectId cannot be found in the current store.
     */
    public void addTaskToProject(String projectId, Task task) {
        if (projectId == null || task == null) {
            throw new IllegalArgumentException("Target Project identifier and Task components must be provided.");
        }

        Project existingProject = projectRepository.findById(projectId);
        if (existingProject == null) {
             throw new IllegalArgumentException("Targeted Project does not exist.");
        }
        
        existingProject.addTask(task);
        projectRepository.update(existingProject); // Merges the modified relation structure back to the Repo
    }

    /**
     * @brief Retrieves all registered projects from the repository.
     * @return List containing all Project instances.
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
