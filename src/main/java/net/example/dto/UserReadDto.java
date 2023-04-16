package net.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Data
public class UserReadDto {

    private final Long id;

    private final String name;

    private final String email;

    @Builder.Default
    private final List<Long> eventsId = new ArrayList<>();

    @Builder.Default
    private final List<Long> filesId = new ArrayList<>();
}
