package com.example.demo.repo;

import com.example.demo.models.Product;
import com.example.demo.models.Storage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StorageRepository extends CrudRepository<Storage,Long> {
    Storage getAllByProductId(Long id);
}
