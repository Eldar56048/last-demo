package com.example.demo.controllers;

import com.example.demo.models.Order;
import com.example.demo.models.Status;
import com.example.demo.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('CASHIER')||hasAuthority('ADMIN')")
@RequestMapping("/cashier")
public class CashierController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String getCashierPage(Model model){
        Iterable<Order> orders = orderRepository.findAllByStatus(Status.WENTCASHIER);
        model.addAttribute("orders",orders);
        return "orders-cashier";
    }

    @GetMapping("/order/{id}")
    public String getOrderPageForCashier(Model model, @PathVariable(value = "id") Long id){
        Optional<Order> order = orderRepository.findById(id);
        ArrayList<Order> result = new ArrayList<>();
        order.ifPresent(result::add);
        model.addAttribute("order", result);
        return "order-info-cashier";
    }


}
