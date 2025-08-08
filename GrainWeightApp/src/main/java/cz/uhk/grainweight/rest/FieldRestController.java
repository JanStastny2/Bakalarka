package cz.uhk.grainweight.rest;

import cz.uhk.grainweight.model.Field;
import cz.uhk.grainweight.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
public class FieldRestController {

    private final FieldService fieldService;

    @Autowired
    public FieldRestController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @GetMapping("/getall")
    public List<Field> getAllFields() {
        return fieldService.getAllFields();
    }

    @GetMapping("/get/{id}")
    public Field getField(@PathVariable long id) {
        return fieldService.getField(id);
    }

    @PostMapping("/new")
    public Field createOrUpdateField(@RequestBody Field field) {
        fieldService.saveField(field);
        return field;
    }

    @DeleteMapping("/delete/{id}")
    public Field deleteField(@PathVariable long id) {
        Field field = fieldService.getField(id);
        if (field != null) {
            fieldService.deleteField(id);
        }
        return field;
    }
}
