package com.example.demo.repo;

import com.example.demo.models.Type;
import org.springframework.data.repository.CrudRepository;

public interface TypeRepository extends CrudRepository<Type,Long> {
    Type getAllById(Long id);
}
