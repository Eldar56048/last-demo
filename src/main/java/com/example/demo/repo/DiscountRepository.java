package com.example.demo.repo;

import com.example.demo.models.Client;
import com.example.demo.models.Discount;
import org.springframework.data.repository.CrudRepository;

public interface DiscountRepository extends CrudRepository<Discount,Long> {
    Discount getAllById(Long id);
}
