package com.booky.service;

import com.booky.model.entity.Role;
import com.booky.model.entity.User;
import com.booky.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }

        User user = userOpt.get();
        List<GrantedAuthority> authorities =
                buildUserAuthority(user.getRoleList());
        return buildUserForAuthentication(user, authorities);

    }

    public org.springframework.security.core.userdetails.User buildUserForAuthentication(User user,
                                                                                          List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, authorities);
    }

    public List<GrantedAuthority> buildUserAuthority(List<Role> userRoles) {
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }

        return userRoles.stream().map(role -> new SimpleGrantedAuthority(role.getLabel())).collect(Collectors.toList());
    }
}
