package cz.uhk.loadtesterapp.service;

import cz.uhk.loadtesterapp.model.entity.User;
import cz.uhk.loadtesterapp.repository.UserRepository;
import cz.uhk.loadtesterapp.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public User updateUser(User user, Long id) {
        User existUser = userRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        existUser.setUsername(user.getUsername());
        existUser.setEmail(user.getEmail());
        existUser.setRole(user.getRole());
        return userRepository.save(existUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void adminSetPassword(Long id, String newPassword) {
        if (newPassword == null || newPassword.isBlank())
            throw new IllegalArgumentException("Password cannot be blank");
        User user = userRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.isBlank())
            throw new IllegalArgumentException("Password cannot be blank");
        User user = userRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new IllegalArgumentException("Password does not match");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user);
    }
}
