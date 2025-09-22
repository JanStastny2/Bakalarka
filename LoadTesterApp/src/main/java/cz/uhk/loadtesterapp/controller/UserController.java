package cz.uhk.loadtesterapp.controller;

import cz.uhk.loadtesterapp.mapper.UserMapper;
import cz.uhk.loadtesterapp.model.dto.*;
import cz.uhk.loadtesterapp.model.entity.User;
import cz.uhk.loadtesterapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.http.client.reactive.AbstractClientHttpConnectorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final AbstractClientHttpConnectorProperties abstractClientHttpConnectorProperties;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users.stream()
                .map(userMapper::toUserDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
//        log.info("GET /api/users/{}", id);
        var user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(userMapper.toUserDto(user))
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserCreateRequest req, UriComponentsBuilder uriBuilder) {
//        log.info("POST /api/users (username={})", data.getUsername());
        var saved = userService.saveUser(userMapper.toEntity(req));
        var dto = userMapper.toUserDto(saved);
        var location = uriBuilder.buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserUpdateRequest req) {
        var entity = userService.getUserById(id);
        if (entity == null)
            return ResponseEntity.notFound().build();

        userMapper.updateEntity(req, entity);
        var saved = userService.updateUser(entity, id);
        return ResponseEntity.ok(userMapper.toUserDto(saved));
    }

    //prihlaseny uzivatel
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        var user = userService.findByUsername(principal.getUsername());
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal,
                                                 @RequestBody @Valid ChangePasswordRequest body) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        var user = userService.findByUsername(principal.getUsername());
        if (user == null)
            return ResponseEntity.notFound().build();
        userService.changePassword(user.getId(), body.oldPassword(), body.newPassword());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> adminSetPassword(@RequestBody AdminSetPasswordRequest body, @PathVariable Long id) {
        userService.adminSetPassword(id, body.newPassword());
        return ResponseEntity.ok().build();
    }
}
