package net.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import net.example.domain.entity.User;
import net.example.dto.UserReadDto;

@RequiredArgsConstructor
public class UserReadMapper implements Mapper<UserReadDto, User> {

    private final EventReadMapper eventReadMapper;

    @Override
    public UserReadDto mapFrom(User source) {
        return UserReadDto.builder()
            .id(source.getId())
            .name(source.getName())
            .email(source.getEmail())
            .build();
    }
}
