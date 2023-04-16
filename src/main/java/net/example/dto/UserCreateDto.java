package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Data
public class UserCreateDto {

    private final String name;

    private final String email;

    private final byte[] password;

    @Builder.Default
    private final List<Long> eventsId = new ArrayList<>();

    @Builder.Default
    private final List<Long> filesId = new ArrayList<>();
}
