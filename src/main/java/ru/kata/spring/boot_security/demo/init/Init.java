package ru.kata.spring.boot_security.demo.init;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Set;

@Component
public class Init implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public Init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Role adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        roleRepository.save(userRole);

        Set<Role> userRoles = Set.of(userRole);
        Set<Role> adminRoles = Set.of(adminRole, userRole);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(bCryptPasswordEncoder.encode("admin"));
        admin.setAge(30);
        admin.setEmail("admin@yandex.ru");
        admin.setRoles(adminRoles);
        userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setPassword(bCryptPasswordEncoder.encode("user"));
        user.setAge(30);
        user.setEmail("user@yandex.ru");
        user.setRoles(userRoles);
        userRepository.save(user);
    }
}
