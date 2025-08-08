package cz.uhk.grainweight.service;

import cz.uhk.grainweight.model.Field;
import java.util.List;

public interface FieldService {
    List<Field> getAllFields();
    Field getField(Long id);
    Field saveField(Field field);
    void deleteField(Long id);
}