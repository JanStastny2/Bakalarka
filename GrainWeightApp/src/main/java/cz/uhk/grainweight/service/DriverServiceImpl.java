package cz.uhk.grainweight.service;

import cz.uhk.grainweight.model.Driver;
import cz.uhk.grainweight.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public void saveDriver(Driver driver) {
        driverRepository.save(driver);
    }

    @Override
    public Driver getDriver(long id) {
        return driverRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDriver(long id) {
        driverRepository.deleteById(id);
    }
}
