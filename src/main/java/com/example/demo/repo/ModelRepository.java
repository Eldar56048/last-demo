package com.example.demo.repo;

import com.example.demo.models.Model;
import org.springframework.data.repository.CrudRepository;

public interface ModelRepository extends CrudRepository<Model,Long> {
    Model getAllById(Long id);
}
