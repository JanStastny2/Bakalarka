package cz.uhk.grainweight.controller;

import cz.uhk.grainweight.model.Field;
import cz.uhk.grainweight.model.User;
import cz.uhk.grainweight.model.WeightRecord;
import cz.uhk.grainweight.service.FieldService;
import cz.uhk.grainweight.service.UserService;
import cz.uhk.grainweight.service.WeightRecordService;
import cz.uhk.grainweight.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/weightrecords")
public class WeightRecordController {

    @Autowired
    private WeightRecordService weightRecordService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @GetMapping(path = { "", "/" })
    public String listWeightRecords(Model model) {
        model.addAttribute("weightrecords", weightRecordService.getAllWeightRecords());
        return "weightrecords_list";
    }

    @GetMapping("/new")
    public String newWeightRecordForm(Model model) {
        model.addAttribute("weightrecord", new WeightRecord());
        model.addAttribute("fields", fieldService.getAllFields());
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "weightrecord_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("weightrecord") WeightRecord record,
                       @AuthenticationPrincipal UserDetails userDetails) {

        // Získání přihlášeného uživatele
        User user = userService.findByUsername(userDetails.getUsername());
        record.setCreatedBy(user);

        // Pokud není datum vyplněno, nastav aktuální
        if (record.getDate() == null) {
            record.setDate(LocalDateTime.now());
        }

        weightRecordService.saveWeightRecord(record);
        return "redirect:/weightrecords";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        WeightRecord record = weightRecordService.getWeightRecord(id);
        model.addAttribute("weightrecord", record);
        model.addAttribute("fields", fieldService.getAllFields());
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "weightrecord_form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        weightRecordService.deleteWeightRecord(id);
        return "redirect:/weightrecords";
    }
}
