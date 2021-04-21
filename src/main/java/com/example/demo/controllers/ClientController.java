package com.example.demo.controllers;

import com.example.demo.models.Client;
import com.example.demo.models.Discount;
import com.example.demo.models.Order;
import com.example.demo.repo.ClientRepository;
import com.example.demo.repo.DiscountRepository;
import com.example.demo.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
@PreAuthorize("hasAuthority('ADMIN')")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private OrderRepository orderRepository;
    @GetMapping
    public String getClientsList(Model model){
        Iterable<Client> clients = clientRepository.findAll();
        Iterable<Discount> discounts = discountRepository.findAll();
        model.addAttribute("discounts",discounts);
        model.addAttribute("clients",clients);
        return "clients";
    }

    @PostMapping("/add")
    public String addClient(@RequestParam String clientName,@RequestParam String clientSurname,@RequestParam String phoneNumber,@RequestParam Long discountId, Model model){
        Discount discount = discountRepository.getAllById(discountId);
        Client client = new Client(clientName,clientSurname,phoneNumber,discount);
        clientRepository.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/remove")
    public String clientRemove(@PathVariable(value = "id")long id, Model model) {
        discountRepository.deleteById(id);
        return "redirect:/clients";
    }

    @GetMapping("/{id}")
    public String aboutClient(@PathVariable(value = "id")long id, Model model){
        Client client = clientRepository.getAllById(id);
        Iterable<Order> orders = orderRepository.findAllByClient(client);
        model.addAttribute("orders",orders);
        model.addAttribute("client",client);
        return "client-info";
    }

    @GetMapping("/{id}/update")
    public String getClientForUpdating(@PathVariable(value = "id")long id,Model model){
        Client client = clientRepository.getAllById(id);
        Iterable<Discount> discounts = discountRepository.findAll();
        model.addAttribute("discounts",discounts);
        model.addAttribute("client",client);
        return "client-update";
    }

    @PostMapping("/{id}/update")
    public String updateClient(@PathVariable(value = "id")long id, @RequestParam String clientName,@RequestParam String clientSurname,@RequestParam String phoneNumber,@RequestParam Long discountId, Model model) throws ClassNotFoundException {
        Client client = clientRepository.getAllById(id);
        Discount discount = discountRepository.getAllById(discountId);
        client.setClientName(clientName);
        client.setClientSurname(clientSurname);
        client.setPhoneNumber(phoneNumber);
        client.setDiscount(discount);
        clientRepository.save(client);
        return "redirect:/clients/"+id;
    }
}
