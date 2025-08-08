package cz.uhk.grainweight.rest;

import cz.uhk.grainweight.model.Driver;
import cz.uhk.grainweight.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverRestController {

    private final DriverService driverService;

    @Autowired
    public DriverRestController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/getall")
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/get/{id}")
    public Driver getDriver(@PathVariable long id) {
        return driverService.getDriver(id);
    }

    @PostMapping("/new")
    public Driver createOrUpdateDriver(@RequestBody Driver driver) {
        driverService.saveDriver(driver);
        return driver;
    }

    @DeleteMapping("/delete/{id}")
    public Driver deleteDriver(@PathVariable long id) {
        Driver driver = driverService.getDriver(id);
        if (driver != null) {
            driverService.deleteDriver(id);
        }
        return driver;
    }
}
