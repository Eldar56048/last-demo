package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repo.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/types")
public class TypesController {
    @Autowired
    TypeRepository typeRepository;

    @GetMapping
    public String get_types_Page(Model model){
        Iterable<Type> types = typeRepository.findAll();
        model.addAttribute("types",types);
        return "types";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String type_name, Model model){
        Type type = new Type(type_name);
        typeRepository.save(type);
        return "redirect:/types";
    }

    @GetMapping("/{id}/remove")
    public String serviceRemove(@PathVariable(value = "id")long id,Model model) {
        typeRepository.deleteById(id);
        return "redirect:/types";
    }

    @GetMapping("/{id}")
    public String aboutType(@PathVariable(value = "id")long id, Model model){
        Optional<Type> type = typeRepository.findById(id);
        ArrayList<Type> result = new ArrayList<>();
        type.ifPresent(result::add);
        model.addAttribute("type",result);
        return "type-info";
    }

    @GetMapping("/{id}/update")
    public String getTypeForUpdating(@PathVariable(value = "id")long id,Model model){
        Optional<Type> type = typeRepository.findById(id);
        ArrayList<Type> result = new ArrayList<>();
        type.ifPresent(result::add);
        model.addAttribute("type",result);
        return "type-update";
    }

    @PostMapping("/{id}/update")
    public String updateService(@PathVariable(value = "id")long id, @RequestParam String name, Model model) throws ClassNotFoundException {
        Type type = typeRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        type.setName(name);
        typeRepository.save(type);
        return "redirect:/types/"+type.getId();
    }

}
