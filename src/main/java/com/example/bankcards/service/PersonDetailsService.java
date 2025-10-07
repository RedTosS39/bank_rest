package com.example.bankcards.service;

import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.repository.PeopleRepository;
import com.example.bankcards.security.PersonDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = peopleRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    return new UsernameNotFoundException(username);
                });

        log.info("User found: username={}, password={}, role={}",
                user.getUsername(),
                user.getPassword(),
                user.getRole());

        PersonDetails personDetails = new PersonDetails(user);
        log.info("Authorities: {}", personDetails.getAuthorities());

        return personDetails;
    }
}
