package com.example.demo.repo;
import com.example.demo.models.OrderItem;
import com.example.demo.models.Product;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem,Long> {
    List<OrderItem> getAllByProduct(Product product);
}
