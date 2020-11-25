package com.example.demo.repo;

import com.example.demo.models.RecievingHistory;
import org.springframework.data.repository.CrudRepository;

public interface RecievingHistoryRepository extends CrudRepository<RecievingHistory,Long> {
    RecievingHistory getAllByOrderItemId(long id);
    Iterable<RecievingHistory> getAllByOrderItemProductId(long id);
}
