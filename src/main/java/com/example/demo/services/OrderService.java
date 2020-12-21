package com.example.demo.services;

import com.example.demo.models.Order;
import com.example.demo.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Page<Order> findPaginated(int pageNo,int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1,pageSize);
        return this.orderRepository.findAll(pageable);
    }
}
