package com.example.demo.repo;

import com.example.demo.models.IncomingHistory;
import com.example.demo.models.Product;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface IncomingHistoryRepository extends CrudRepository<IncomingHistory,Long> {
    Iterable<IncomingHistory> getAllByProductId(Long id);
    Iterable<IncomingHistory> getFirstByProductIdOrderByDateDesc(Long id);
    IncomingHistory getById(Long id);
    @Transactional
    void deleteAllByProduct(Product product);
}
