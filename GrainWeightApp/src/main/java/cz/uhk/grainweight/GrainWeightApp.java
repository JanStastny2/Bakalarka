package cz.uhk.grainweight;

import cz.uhk.grainweight.model.Field;
import cz.uhk.grainweight.model.Driver;
import cz.uhk.grainweight.model.User;
import cz.uhk.grainweight.service.FieldService;
import cz.uhk.grainweight.service.DriverService;
import cz.uhk.grainweight.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class GrainWeightApp {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DriverService driverService;
    private final FieldService fieldService;

    @Autowired
    public GrainWeightApp(UserService userService, PasswordEncoder passwordEncoder, DriverService driverService, FieldService fieldService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.driverService = driverService;
        this.fieldService = fieldService;
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            addUser("User", "user", "heslo", "USER");
            addUser("Admin", "admin", "heslo", "ADMIN");

            // Inicializace řidičů
            addDriver("Jan Novák", 5000, "ABC-1234", "jan.novak@example.com");
            addDriver("Petr Svoboda", 5200, "XYZ-5678", "petr.svoboda@example.com");

            // Inicializace polí
            addField("Pole 1", 15.2, "Severní okraj farmy");
            addField("Pole 2", 8.7, "Jižní svah zahrady");
        };
    }

    private void addUser(String name, String username, String password, String role){
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        userService.saveUser(user);
    }

    private void addDriver(String name, int tareWeight, String licencePlate, String contact) {
        Driver driver = new Driver();
        driver.setDriverName(name);
        driver.setTareWeight(tareWeight);
        driver.setLicencePlate(licencePlate);
        driver.setContact(contact);
        driverService.saveDriver(driver);
    }

    private void addField(String name, Double area, String location) {
        Field field = new Field();
        field.setName(name);
        field.setArea(area);
        field.setLocation(location);
        fieldService.saveField(field);
    }

    public static void main(String[] args) {
        SpringApplication.run(GrainWeightApp.class, args);
    }

}
