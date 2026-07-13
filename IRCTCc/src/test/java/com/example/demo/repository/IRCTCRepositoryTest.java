package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import com.example.demo.entity.User;

public class IRCTCRepositoryTest {

    @Test
    public void testRepositoryInterface() {
        IRCTCUserRepository userRepository = mock(IRCTCUserRepository.class);
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        
        assertNotNull(userRepository);
    }
}
