package com.example.demo.repo;

import com.example.demo.models.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
    Client getAllById(Long id);
}
