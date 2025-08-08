package cz.uhk.grainweight.rest;

import cz.uhk.grainweight.model.ApiResponse;
import cz.uhk.grainweight.model.User;
import cz.uhk.grainweight.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController extends BaseController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<User>>> getUsers() {
        return wrapResponse(userService::getAllUsers, HttpStatus.OK,"Users retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        User user = userService.getUser(id);
        if (user == null)
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        User user = userService.getUser(id);
        if (user == null)
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        else{
            userService.deleteUser(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable long id) {
        try {
            userService.updateUser(id, user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/new")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        return wrapResponse(() -> userService.saveUser(user), HttpStatus.CREATED, "succes");
    }

}
