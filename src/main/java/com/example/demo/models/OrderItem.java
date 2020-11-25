package com.example.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int quantity;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Service service;
    @ManyToOne
    private Product product;
    public OrderItem(){

    }
    public OrderItem(int quantity, Order order, Service service) {
        this.quantity = quantity;
        this.order = order;
        this.service = service;
    }
    public OrderItem(int quantity, Order order, Product product) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
