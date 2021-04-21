package com.example.demo.controllers;

import com.example.demo.repo.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/models")
public class ModelController {
    @Autowired
    ModelRepository modelRepository;

    @GetMapping
    public String get_models_Page(Model model){
        Iterable<com.example.demo.models.Model> models = modelRepository.findAll();
        model.addAttribute("models",models);
        return "models";
    }

    @PostMapping("/add")
    public String addModel(@RequestParam String model_name, Model model){
        com.example.demo.models.Model model1 = new com.example.demo.models.Model(model_name);
        modelRepository.save(model1);
        return "redirect:/models";
    }

    @GetMapping("/{id}/remove")
    public String modelRemove(@PathVariable(value = "id")long id, Model model) {
        modelRepository.deleteById(id);
        return "redirect:/models";
    }

    @GetMapping("/{id}")
    public String aboutModel(@PathVariable(value = "id")long id, Model model){
        Optional<com.example.demo.models.Model> modelOptional = modelRepository.findById(id);
        ArrayList<com.example.demo.models.Model> result = new ArrayList<>();
        modelOptional.ifPresent(result::add);
        model.addAttribute("model",result);
        return "model-info";
    }

    @GetMapping("/{id}/update")
    public String getModelForUpdating(@PathVariable(value = "id")long id,Model model){
        Optional<com.example.demo.models.Model> modelOptional = modelRepository.findById(id);
        ArrayList<com.example.demo.models.Model> result = new ArrayList<>();
        modelOptional.ifPresent(result::add);
        model.addAttribute("model",result);
        return "model-update";
    }

    @PostMapping("/{id}/update")
    public String updateModel(@PathVariable(value = "id")long id, @RequestParam String name, Model model) throws ClassNotFoundException {
        com.example.demo.models.Model model1 = modelRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        model1.setName(name);
        modelRepository.save(model1);
        return "redirect:/models/"+model1.getId();
    }
}
