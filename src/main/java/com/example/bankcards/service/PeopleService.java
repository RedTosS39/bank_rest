package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.repository.PeopleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private ModelMapper modelMapper;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = peopleRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto findUserById(Long id) throws UsernameNotFoundException {
        UserEntity user = peopleRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id:" + id + " not found"));
        return modelMapper.map(user, UserDto.class);
    }
}
