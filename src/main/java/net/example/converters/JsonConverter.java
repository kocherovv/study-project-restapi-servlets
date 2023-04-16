package net.example.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.example.domain.entity.File;
import net.example.util.AppContainer;

import javax.persistence.AttributeConverter;

public class JsonConverter implements AttributeConverter<File, String> {
    private final ObjectMapper jsonMapper = AppContainer.getInstance().getJsonMapper();

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(File attribute) {
        return jsonMapper.writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public File convertToEntityAttribute(String dbData) {
        return jsonMapper.readValue(dbData, File.class);
    }
}
