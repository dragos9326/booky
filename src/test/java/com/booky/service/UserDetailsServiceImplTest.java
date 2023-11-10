package com.booky.service;

import com.booky.exceptions.GeneralException;
import com.booky.model.dto.BookingDTO;
import com.booky.model.entity.Booking;
import com.booky.model.entity.Property;
import com.booky.model.entity.Role;
import com.booky.model.entity.User;
import com.booky.repository.PropertyRepository;
import com.booky.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsernameWhenNotFoundThenThrow() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("email"));
    }

    @Test
    void testLoadUserByUsernameWhenFoundThenCallBuild() {
        User user = new User();
        user.setRoleList(new ArrayList<>());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        List<GrantedAuthority> list = new ArrayList<>();
        org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User("name", "pass", true, true, true, true, list);

        UserDetailsServiceImpl spy = Mockito.spy(userDetailsService);
        doReturn(list).when(spy).buildUserAuthority(anyList());
        doReturn(springUser).when(spy).buildUserForAuthentication(any(), any());

        UserDetails result = spy.loadUserByUsername("email");
        assertEquals(result, springUser);
    }

    @Test
    void testBuildUserForAuthenticationWhenGoodThenReturnUser() {
        User user = new User();
        String email = "email";
        String password = "password";
        user.setEmail(email);
        user.setPassword(password);
        List<GrantedAuthority> list = new ArrayList<>();


        org.springframework.security.core.userdetails.User result = userDetailsService.buildUserForAuthentication(user, list);
        assertEquals(result.getUsername(), email);
        assertEquals(result.getPassword(), password);
    }

    @Test
    void testBbuildUserAuthorityWhenEmptyThenReturnEmpty() {
        List<Role> roles = new ArrayList<>();

        List<GrantedAuthority> result = userDetailsService.buildUserAuthority(roles);
        assertEquals(0, result.size());
    }

    @Test
    void testBbuildUserAuthorityWhenMoreThenReturnSameLength() {
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        String label = "Test";
        role.setLabel(label);
        roles.add(role);

        List<GrantedAuthority> result = userDetailsService.buildUserAuthority(roles);
        assertEquals(roles.size(), result.size());
        assertEquals(label, result.get(0).getAuthority());
    }

}