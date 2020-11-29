package com.example.demo.repo;

import com.example.demo.models.Order;
import com.example.demo.models.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface OrderRepository extends CrudRepository<Order,Long> {
    Iterable<Order> findByOrderByIdDesc();
    Order getAllById(Long id);
    Iterable<Order> findAllByStatus(Status status);
    /*Iterable<Order> getAllByAccepted_dateBetween(Date date1,Date date2);*/
}
