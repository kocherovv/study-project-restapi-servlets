package net.example.dto.mapper;

import net.example.domain.entity.User;
import net.example.dto.UserCreateDto;

public class UserCreateMapper implements Mapper<User, UserCreateDto> {
    @Override
    public User mapFrom(UserCreateDto source) {
        return User.builder()
            .name(source.getName())
            .email(source.getEmail())
            .build();
    }
}
