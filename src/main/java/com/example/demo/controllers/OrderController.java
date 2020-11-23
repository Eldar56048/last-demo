package com.example.demo.controllers;

import com.example.demo.models.Order;
import com.example.demo.models.Status;
import com.example.demo.models.User;
import com.example.demo.repo.OrderRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @PostMapping("/orders/add")
    public String ordersAdd(@AuthenticationPrincipal User user,@RequestParam String client_name,@RequestParam String client_number, @RequestParam String problem, Model model){
        System.out.println("Hello");
        System.out.println(client_name+" "+client_number+" "+problem);
        Order order = new Order(client_name,client_number,problem,user);
        orderRepository.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderById(@PathVariable(value = "id") long id, Model model){
        Optional<Order> order = orderRepository.findById(id);
        ArrayList<Order> result = new ArrayList<>();
        order.ifPresent(result::add);
        model.addAttribute("order",result);
        return "order-info";
    }
    @GetMapping("/orders/{id}/update")
    public String orderUpdateGet(@PathVariable(value = "id") long id,Model model){
        Optional<Order> order = orderRepository.findById(id);
        ArrayList<Order> result = new ArrayList<>();
        order.ifPresent(result::add);
        model.addAttribute("order",result);
        return "order-update";
    }

    @PostMapping("/orders/{id}/update")
    public String orderUpdatePost(@AuthenticationPrincipal User user,@PathVariable(value = "id") long id,@RequestParam String what_done, @RequestParam int price, Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        order.setDoneUser(user);
        order.setWhat_done(what_done);
        order.setPrice(price);
        order.setStatus(Status.DONE);
        orderRepository.save(order);
        return "redirect:/orders/"+id;
    }

    @GetMapping("/orders/{id}/given")
    public String orderGiven(@AuthenticationPrincipal User user,@PathVariable(value = "id") long id, Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        order.setGave_date(new Date());
        order.setStatus(Status.GIVEN);
        order.setGiveUser(user);
        orderRepository.save(order);
        return "redirect:/orders/"+id;
    }

    @GetMapping("/orders/{id}/remove")
    public String orderRemove(@PathVariable(value = "id")long id,Model model) throws ClassNotFoundException {
        orderRepository.deleteById(id);
        return "redirect:/orders";
    }

}
