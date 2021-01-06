package com.example.demo.controllers;

import com.example.demo.models.Discount;
import com.example.demo.repo.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/discounts")
@PreAuthorize("hasAuthority('ADMIN')")
public class DiscountController {
    @Autowired
    private DiscountRepository discountRepository;
    @GetMapping
    public String getDiscounts(Model model){
        Iterable<Discount> discounts = discountRepository.findAll();
        model.addAttribute("discounts",discounts);
        return "discounts";
    }

    @PostMapping("/add")
    public String addDiscount(@RequestParam String discountName,@RequestParam int percentage, Model model){
        Discount discount = new Discount(discountName,percentage);
        discountRepository.save(discount);
        return "redirect:/discounts";
    }

    @GetMapping("/{id}/remove")
    public String discountRemove(@PathVariable(value = "id")long id, Model model) {
        discountRepository.deleteById(id);
        return "redirect:/discounts";
    }

    @GetMapping("/{id}")
    public String aboutDiscount(@PathVariable(value = "id")long id, Model model){
        Discount discount = discountRepository.getAllById(id);
        model.addAttribute("discount",discount);
        return "discount-info";
    }

    @GetMapping("/{id}/update")
    public String getModelForUpdating(@PathVariable(value = "id")long id,Model model){
        Discount discount = discountRepository.getAllById(id);
        model.addAttribute("discount",discount);
        return "discount-update";
    }

    @PostMapping("/{id}/update")
    public String updateDiscount(@PathVariable(value = "id")long id, @RequestParam String discountName,@RequestParam int percentage, Model model) throws ClassNotFoundException {
        Discount discount = discountRepository.getAllById(id);
        discount.setDiscountName(discountName);
        discount.setPercentage(percentage);
        discountRepository.save(discount);
        return "redirect:/discounts/"+id;
    }
}
