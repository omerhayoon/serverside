package com.dev.services;

import com.dev.models.User;
import com.dev.repository.UserRepository;
import com.dev.utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InputValidator inputValidator;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }
    public boolean isUsernameOrEmailTaken(String username, String email) {
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }

    public User registerUser(String name, String username, String password, String confirmPassword, String email) {
        System.out.println("entered register");

        if (isUsernameOrEmailTaken(username, email)) {
            System.out.println("username/email is already taken");
            throw new RuntimeException("Username/email is already taken");
        }

        List<String> passwordErrors = inputValidator.validatePassword(password, confirmPassword);
        if (!passwordErrors.isEmpty()) {
            throw new RuntimeException("Invalid password");
        }

        // יצירת אובייקט משתמש חדש
        User user = new User();
        user.setName(name); // הגדרת השם של המשתמש
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setAdmin(false);

        // שמירת המשתמש בבסיס הנתונים
        return userRepository.save(user);
    }


    public User login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(null);
    }
}
