package com.example.demo.repo;

import com.example.demo.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Long> {
    Product getAllById(Long id);
}
