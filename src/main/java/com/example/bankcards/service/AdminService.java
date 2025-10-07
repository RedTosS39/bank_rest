package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminService {

    private final PeopleRepository peopleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminService(PeopleRepository peopleRepository, ModelMapper modelMapper) {
        this.peopleRepository = peopleRepository;
        this.modelMapper = modelMapper;
    }


    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = peopleRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        log.info("User has been found: {}", user.getUsername());
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto findUserById(Long id) throws UsernameNotFoundException {
        UserEntity user = peopleRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id:" + id + " not found"));
        log.info("User has been found{}", user.getId());
        return modelMapper.map(user, UserDto.class);
    }
}
