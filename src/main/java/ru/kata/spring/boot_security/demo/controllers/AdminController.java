package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getUsers(@ModelAttribute("user") User user, ModelMap modelMap, Principal principal) {
        Optional<User> currentUser = userService.getByUsername(principal.getName());
        List<User> users = userService.getUsers();
        List<Role> roleList = roleService.getRoles();
        modelMap.addAttribute("users", users);
        modelMap.addAttribute("roleList", roleList);
        modelMap.addAttribute("currentUser", currentUser.orElseThrow(
                () -> new UsernameNotFoundException("Principal user not found")));
        return "admin";
    }

    @PostMapping(value = "/create")
    public String createUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/update")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("id") Long id) {
        if (userService.getById(id).isEmpty()) {
            return "notfound";
        }
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        if (userService.getById(id).isEmpty()) {
            return "notfound";
        }
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}