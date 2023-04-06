package net.example.service;

import java.util.List;
import java.util.Optional;

public interface CrudService <CreateDTO, ReadDTO, ID>{

    List<ReadDTO> findAll();

    Optional<ReadDTO> findById(ID id);

    ReadDTO create(CreateDTO dto);

    ReadDTO update(ReadDTO dto);

    void deleteById(ID entity);
}
