package net.example.dto.mapper;

public interface Mapper <T, S> {
    T mapFrom(S source);
}
