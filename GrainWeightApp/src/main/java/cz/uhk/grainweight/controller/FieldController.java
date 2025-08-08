package cz.uhk.grainweight.controller;

import cz.uhk.grainweight.model.Field;
import cz.uhk.grainweight.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fields")
public class FieldController {

    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @GetMapping(path = { "", "/" })
    public String listFields(Model model) {
        model.addAttribute("fields", fieldService.getAllFields());
        return "fields_list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("field", new Field());
        return "field_form";
    }

    @PostMapping("/save")
    public String saveField(@ModelAttribute("field") Field field) {
        fieldService.saveField(field);
        return "redirect:/fields";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Field field = fieldService.getField(id);
        model.addAttribute("field", field);
        return "field_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
        return "redirect:/fields";
    }
}