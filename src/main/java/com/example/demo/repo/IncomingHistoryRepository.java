package com.example.demo.repo;

import com.example.demo.models.IncomingHistory;
import org.springframework.data.repository.CrudRepository;

public interface IncomingHistoryRepository extends CrudRepository<IncomingHistory,Long> {
    Iterable<IncomingHistory> getAllByProductId(Long id);
}
