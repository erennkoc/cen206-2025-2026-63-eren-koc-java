package com.projectmanagement.lib.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.projectmanagement.lib.models.User;
import com.projectmanagement.lib.storage.IRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IRepository<User> userRepositoryMock;

    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepositoryMock);
    }

    @Test
    public void testRegisterUser_Success() {
        String id = "uuid-1";
        String username = "testuser";
        String email = "test@user.com";
        String plainPassword = "SuperSecretPassword";

        userService.registerUser(id, username, email, plainPassword);

        // Capture what was passed to create()
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock, times(1)).create(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(username, capturedUser.getUsername());
        assertEquals(email, capturedUser.getEmail());
        assertNotEquals(plainPassword, capturedUser.getPassword(), "Password must be hashed, not plaintext!");
    }

    @Test
    public void testRegisterUser_NullFields_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(null, "username", "email", "password");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("id", null, "email", "password");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("id", "username", null, "password");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("id", "username", "email", ""); // Empty string
        });

        // Verify create was NEVER called
        verify(userRepositoryMock, never()).create(any(User.class));
    }

    @Test
    public void testLogin_Success() throws Exception {
        String username = "testuser";
        String plainPassword = "SuperSecretPassword";
        
        java.lang.reflect.Method method = UserService.class.getDeclaredMethod("hashPassword", String.class);
        method.setAccessible(true);
        String expectedHashedPassword = (String) method.invoke(userService, plainPassword);

        User mockedUser = new User("uuid-1", username, "test@user.com", expectedHashedPassword);
        
        when(userRepositoryMock.findAll()).thenReturn(Arrays.asList(mockedUser));

        User loggedInUser = userService.login(username, plainPassword);

        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        String username = "testuser";
        String plainPassword = "SuperSecretPassword";
        String wrongPassword = "WrongPassword!";
        
        java.lang.reflect.Method method = UserService.class.getDeclaredMethod("hashPassword", String.class);
        method.setAccessible(true);
        String expectedHashedPassword = (String) method.invoke(userService, plainPassword);

        User mockedUser = new User("uuid-1", username, "test@user.com", expectedHashedPassword);
        when(userRepositoryMock.findAll()).thenReturn(Arrays.asList(mockedUser));

        User loggedInUser = userService.login(username, wrongPassword);
        assertNull(loggedInUser, "Should return null for wrong password.");
    }
    
    @Test
    public void testLogin_NullOrEmptyInputs() {
        assertNull(userService.login(null, "password"));
        assertNull(userService.login("username", null));
        assertNull(userService.login("username", ""));
    }

    @Test
    public void testGetAllUsers() {
        java.util.List<User> mockList = Arrays.asList(new User("u-1", "T", "D", "P"));
        when(userRepositoryMock.findAll()).thenReturn(mockList);
        java.util.List<User> result = userService.getAllUsers();
        assertEquals(1, result.size());
        verify(userRepositoryMock, times(1)).findAll();
    }
}
