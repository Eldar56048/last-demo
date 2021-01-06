package com.example.demo.repo;

import com.example.demo.models.Experience;
import com.example.demo.models.ExperienceModel;
import org.springframework.data.repository.CrudRepository;

public interface ExperienceRepository extends CrudRepository<ExperienceModel,Long> {
    ExperienceModel getAllById(Long id);
    ExperienceModel getAllByExperience(Experience experience);
}
