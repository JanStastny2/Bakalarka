package cz.uhk.grainweight.controller;

import cz.uhk.grainweight.model.Driver;
import cz.uhk.grainweight.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping(path = { "", "/" })
    public String list(Model model) {
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "drivers_list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return "redirect:/drivers";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("driver", new Driver());
        return "drivers_add";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable long id) {
        model.addAttribute("driver", driverService.getDriver(id));
        return "drivers_add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Driver driver) {
        driverService.saveDriver(driver);
        return "redirect:/drivers/";
    }

}
