package com.example.demo.controllers;

import com.example.demo.models.Role;
import com.example.demo.models.Type;
import com.example.demo.models.User;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String getUsers(Model model){
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "users";
    }

    @PostMapping("/add")
    public String registration(User user,Model model){
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable(value = "id")long id, Model model){
        User user = userRepository.getById(id);
        model.addAttribute("user",user);
        return "user-info";
    }

    @GetMapping("/{id}/remove")
    public String userRemove(@PathVariable(value = "id")long id,Model model) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/{id}/password")
    public String getUserForUpdatingPassword(@PathVariable(value = "id")long id,Model model){
        User user = userRepository.getById(id);
        model.addAttribute("user",user);
        return "user-password";
    }

    @GetMapping("/{id}/phoneNumber")
    public String getUserForUpdatingPhoneNumber(@PathVariable(value = "id")long id,Model model){
        User user = userRepository.getById(id);
        model.addAttribute("user",user);
        return "user-phoneNumber";
    }

    @PostMapping("/{id}/password")
    public String updateUserPassword(@PathVariable(value = "id")long id, @RequestParam String password, Model model) throws ClassNotFoundException {
        User user = userRepository.getById(id);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/users/"+id;
    }

    @PostMapping("/{id}/phoneNumber")
    public String updateUserPhoneNumber(@PathVariable(value = "id")long id, @RequestParam String phoneNumber, Model model) throws ClassNotFoundException {
        User user = userRepository.getById(id);
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        return "redirect:/users/"+id;
    }

    @GetMapping("/{id}/roles")
    public String getUserRoles(@PathVariable(value = "id")long id,Model model){
        User user = userRepository.getById(id);
        model.addAttribute("user",user);
        Role[] roles = Role.values();
        model.addAttribute("roles",roles);
        return "user-roles";
    }
    @PostMapping("/{id}/roles")
    public String submitUser(@PathVariable(value = "id")long id,@ModelAttribute("user") User user) {
        User userFirst = userRepository.getById(id);
        userFirst.setRoles(user.getRoles());
        userRepository.save(userFirst);
        return "redirect:/users/"+user.getId();
    }

}
