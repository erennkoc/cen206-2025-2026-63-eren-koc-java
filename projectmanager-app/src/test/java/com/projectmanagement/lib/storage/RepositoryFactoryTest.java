package com.projectmanagement.lib.storage;

import static org.junit.jupiter.api.Assertions.*;

import com.projectmanagement.lib.models.Project;
import com.projectmanagement.lib.models.Task;
import com.projectmanagement.lib.models.User;
import org.junit.jupiter.api.Test;

public class RepositoryFactoryTest {

    @Test
    public void testGetRepository_BinaryStorage() {
        StorageConfig config = new StorageConfig(StorageType.BINARY_FILE);
        RepositoryFactory factory = new RepositoryFactory(config);

        IRepository<User> useRepo = factory.getUserRepository();
        assertTrue(useRepo instanceof BinaryUserRepository);

        IRepository<Project> projRepo = factory.getProjectRepository();
        assertTrue(projRepo instanceof BinaryProjectRepository);

        IRepository<Task> taskRepo = factory.getTaskRepository();
        assertTrue(taskRepo instanceof BinaryTaskRepository);
    }

    @Test
    public void testGetRepository_SQLiteStorage() {
        StorageConfig config = new StorageConfig(StorageType.SQLITE);
        RepositoryFactory factory = new RepositoryFactory(config);

        IRepository<User> useRepo = factory.getUserRepository();
        assertTrue(useRepo instanceof SQLiteUserRepository);

        IRepository<Project> projRepo = factory.getProjectRepository();
        assertTrue(projRepo instanceof SQLiteProjectRepository);

        IRepository<Task> taskRepo = factory.getTaskRepository();
        assertTrue(taskRepo instanceof SQLiteTaskRepository);
    }

    @Test
    public void testGetRepository_MySQLStorage() {
        StorageConfig config = new StorageConfig(StorageType.MYSQL);
        RepositoryFactory factory = new RepositoryFactory(config);

        IRepository<User> useRepo = factory.getUserRepository();
        assertTrue(useRepo instanceof MySQLUserRepository);

        IRepository<Project> projRepo = factory.getProjectRepository();
        assertTrue(projRepo instanceof MySQLProjectRepository);

        IRepository<Task> taskRepo = factory.getTaskRepository();
        assertTrue(taskRepo instanceof MySQLTaskRepository);
    }

    @Test
    public void testGetRepository_UnsupportedStorageType() {
        // Technically Java enums enforce type safety so this won't happen normally,
        // but it's good practice to ensure the switch loop handles default fallthrough errors.
        
        // Simulating the edge case where config gets set to null (or someone adds a new enum type but forgets to add it to the factory switch cases)
        StorageConfig config = new StorageConfig(null);
        RepositoryFactory factory = new RepositoryFactory(config);

        assertThrows(NullPointerException.class, factory::getUserRepository);
        assertThrows(NullPointerException.class, factory::getProjectRepository);
        assertThrows(NullPointerException.class, factory::getTaskRepository);
    }
}
