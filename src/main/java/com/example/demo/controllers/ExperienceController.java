package com.example.demo.controllers;

import com.example.demo.models.ExperienceModel;
import com.example.demo.models.Service;
import com.example.demo.models.User;
import com.example.demo.repo.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/experiences")
@PreAuthorize("hasAuthority('ADMIN')")
public class ExperienceController {
    @Autowired
    private ExperienceRepository experienceRepository;

    @GetMapping
    public String getExperiences(Model model){
        Iterable<ExperienceModel> experienceModels = experienceRepository.findAll();
        model.addAttribute("experiences",experienceModels);
        return "experiences";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable(value = "id")long id, Model model){
        ExperienceModel experienceModel = experienceRepository.getAllById(id);
        model.addAttribute("experience",experienceModel);
        return "experience-info";
    }

    @GetMapping("/{id}/update")
    public String getExperienceForUpdating(@PathVariable(value = "id")long id,Model model){
        ExperienceModel experienceModel = experienceRepository.getAllById(id);
        model.addAttribute("experience",experienceModel);
        return "experience-update";
    }
    @PostMapping("/{id}/update")
    public String updateExperience(@PathVariable(value = "id")long id, @RequestParam int coefficient, Model model) throws ClassNotFoundException {
        ExperienceModel experienceModel = experienceRepository.getAllById(id);
        experienceModel.setCoefficient(coefficient);
        experienceRepository.save(experienceModel);
        return "redirect:/experiences/"+experienceModel.getId();
    }

    @GetMapping("/{id}/remove")
    public String experienceRemove(@PathVariable(value = "id")long id,Model model) {
        experienceRepository.deleteById(id);
        return "redirect:/experiences";
    }

}
