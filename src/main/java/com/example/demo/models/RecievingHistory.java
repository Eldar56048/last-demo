package com.example.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "recievingHistory")
public class RecievingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private OrderItem orderItem;
    public RecievingHistory(){}
    public RecievingHistory(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
