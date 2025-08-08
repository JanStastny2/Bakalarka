package cz.uhk.grainweight.service;

import cz.uhk.grainweight.model.Driver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverService {
    List<Driver> getAllDrivers();
    void saveDriver(Driver driver);
    Driver getDriver(long id);
    void deleteDriver(long id);
}
