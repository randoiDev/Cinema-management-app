package com.cinema.userauthservice.runner;

import com.cinema.userauthservice.domain.User;
import com.cinema.userauthservice.domain.enums.UserRole;
import com.cinema.userauthservice.repositories.EmployeeRepository;
import com.cinema.userauthservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@SuppressWarnings("all")
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("password1"));
        user.setRole(UserRole.ADMIN);
        user.setName("ADMIN");
        user.setSurname("ADMIN");
        user.setEnabled(true);
        userRepository.save(user);
    }

}
