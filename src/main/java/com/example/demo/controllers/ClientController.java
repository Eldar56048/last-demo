package com.example.demo.controllers;

import com.example.demo.models.Order;
import com.example.demo.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private OrderRepository orderRepository;
    @GetMapping
    public String getClientPage(Model model){
        return "main";
    }
    @GetMapping("/{id}")
    public String getClientOrder(@PathVariable(value = "id") long id,Model model){
        Order order = orderRepository.getAllById(id);
        model.addAttribute("order",order);
        return "main";
    }
    @PostMapping
    public String orderClient(@RequestParam long id, Model model) {
        Optional<Order> order = orderRepository.findById(id);
        ArrayList<Order> result = new ArrayList<>();
        order.ifPresent(result::add);
        model.addAttribute("order",result);
        return "main";
    }
}
