package com.example.demo.repo;

import com.example.demo.models.Service;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service,Long> {
}
