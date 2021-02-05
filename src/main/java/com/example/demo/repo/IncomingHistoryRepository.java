package com.example.demo.repo;

import com.example.demo.models.IncomingHistory;
import com.example.demo.models.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface IncomingHistoryRepository extends CrudRepository<IncomingHistory,Long> {
    Iterable<IncomingHistory> getAllByProductId(Long id);
    Iterable<IncomingHistory> getFirstByProductIdOrderByDateDesc(Long id);
    IncomingHistory getById(Long id);
}
