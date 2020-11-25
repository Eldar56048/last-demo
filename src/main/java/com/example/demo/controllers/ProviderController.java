package com.example.demo.controllers;

import com.example.demo.models.Order;
import com.example.demo.models.Provider;
import com.example.demo.repo.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/providers")
public class ProviderController {
    @Autowired
    private ProviderRepository providerRepository;
    @GetMapping
    public String getProvidersPage(Model model){
        Iterable<Provider> providers = providerRepository.findAll();
        model.addAttribute("providers",providers);
        return "providers";
    }
    @PostMapping("/add")
    public String addProvider(@RequestParam String name,@RequestParam String address,@RequestParam String telephone,Model model){
        Provider provider = new Provider(name,address,telephone);
        providerRepository.save(provider);
        return "redirect:/providers";
    }

    @GetMapping("/{id}")
    public String aboutProvider(@PathVariable(value = "id")long id,Model model){
        Optional<Provider> provider = providerRepository.findById(id);
        ArrayList<Provider> result = new ArrayList<>();
        provider.ifPresent(result::add);
        model.addAttribute("provider",result);
        return "provider-info";
    }

    @GetMapping("/{id}/remove")
    public String deleteProvider(@PathVariable(value = "id")long id,Model model) throws ClassNotFoundException {
        Provider provider = providerRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        providerRepository.delete(provider);
        return "redirect:/providers";
    }
    @GetMapping("/{id}/update")
    public String getProviderForUpdating(@PathVariable(value = "id")long id,Model model){
        Optional<Provider> provider = providerRepository.findById(id);
        ArrayList<Provider> result = new ArrayList<>();
        provider.ifPresent(result::add);
        model.addAttribute("provider",result);
        return "provider-update";
    }
    @PostMapping("/{id}/update")
    public String updateProvider(@PathVariable(value = "id") long id,@RequestParam String name,@RequestParam String address,@RequestParam String phone, Model model) throws ClassNotFoundException {
        Provider provider = providerRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        provider.setProvider_name(name);
        provider.setProvider_telephone(phone);
        provider.setProvider_address(address);
        providerRepository.save(provider);
        return "redirect:/providers/"+provider.getId();
    }

}
