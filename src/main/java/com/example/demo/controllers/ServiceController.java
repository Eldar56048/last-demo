package com.example.demo.controllers;

import com.example.demo.models.Service;
import com.example.demo.repo.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('ADMIN')||hasAuthority('MODERATOR')")
@RequestMapping("/services")
public class ServiceController {
    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public String getAllServices(Model model){
        Iterable<Service> services = serviceRepository.findAll();
        model.addAttribute("services",services);
        return "services";
    }

    @PostMapping("/add")
    public String addService(@RequestParam String name,@RequestParam String description,@RequestParam int price,Model model){
        serviceRepository.save(new Service(name,description,price));
        return "redirect:/services";
    }

    @GetMapping("/{id}")
    public String aboutService(@PathVariable(value = "id") long id,Model model){
        Optional<Service> service = serviceRepository.findById(id);
        ArrayList<Service> result = new ArrayList<>();
        service.ifPresent(result::add);
        model.addAttribute("service",result);
        return "service-info";
    }

    @GetMapping("/{id}/update")
    public String getServiceForUpdating(@PathVariable(value = "id")long id,Model model){
        Optional<Service> service = serviceRepository.findById(id);
        ArrayList<Service> result = new ArrayList<>();
        service.ifPresent(result::add);
        model.addAttribute("service",result);
        return "service-update";
    }
    @PostMapping("/{id}/update")
    public String updateService(@PathVariable(value = "id")long id,@RequestParam String name,@RequestParam String description,@RequestParam int price,Model model) throws ClassNotFoundException {
        Service service = serviceRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        service.setService_name(name);
        service.setService_description(description);
        service.setPrice(price);
        serviceRepository.save(service);
        return "redirect:/services/"+service.getId();
    }

    @GetMapping("/{id}/remove")
    public String serviceRemove(@PathVariable(value = "id")long id,Model model) {
        serviceRepository.deleteById(id);
        return "redirect:/services";
    }
}
