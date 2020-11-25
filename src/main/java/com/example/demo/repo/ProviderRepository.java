package com.example.demo.repo;

import com.example.demo.models.Provider;
import org.springframework.data.repository.CrudRepository;

public interface ProviderRepository extends CrudRepository<Provider,Long> {
    Provider getAllById(Long id);
}
