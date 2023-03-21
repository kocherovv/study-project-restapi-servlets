package net.example.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Data
public class UserCreateDto {

    private final String name;

    private final String email;

    @Builder.Default
    private final List<Long> events_id = new ArrayList<>();
}
