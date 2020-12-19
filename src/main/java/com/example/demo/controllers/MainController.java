package com.example.demo.controllers;

import com.example.demo.models.Order;
import com.example.demo.models.Role;
import com.example.demo.models.Type;
import com.example.demo.models.User;
import com.example.demo.repo.ModelRepository;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.TypeRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class MainController {
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelRepository modelRepository;
    @GetMapping("/orders")
    public String orders(Model model){
        Iterable<Type> types = typeRepository.findAll();
        Iterable<Order> orders = orderRepository.findByOrderByIdDesc();
        Iterable<com.example.demo.models.Model> models = modelRepository.findAll();
        model.addAttribute("orders",orders);
        model.addAttribute("types",types);
        model.addAttribute("models",models);
        return "orders";
    }

    @GetMapping("/")
    public String main(Model model){
        return "profile";
    }

    @GetMapping("/reg")
    public String reg(Model model){
        return "reg";
    }

    @PostMapping("/reg")
    public String registration(User user,Model model){
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/login";
    }




}
