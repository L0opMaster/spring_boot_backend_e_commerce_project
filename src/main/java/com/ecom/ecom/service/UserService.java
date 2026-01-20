package com.ecom.ecom.service;

import com.ecom.ecom.model.Role;
import com.ecom.ecom.model.User;
import com.ecom.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // REGISTER
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
    }

    // LOGIN
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Account doesn't exist");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Wrong password");
        }

        return user;
    }

    // ADMIN: GET ALL USERS
    public List<User> getAllUsers(String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail);

        if (admin == null || admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied");
        }

        return userRepository.findAll();
    }

    // ADMIN: DELETE USER
    public void deleteUser(Integer id, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail);

        if (admin == null || admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied");
        }

        userRepository.deleteById(id);
    }
}

