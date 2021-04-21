package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(generator = "sequence-order-item-generator")
    @GenericGenerator(
            name = "sequence-order-item-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "orderitm_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "100"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private int quantity;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Service service;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doneUser")
    private User doneUser;
    private int soldPrice;
    private int servicePercentage;
    @ManyToOne
    private ExperienceModel userExperience;
    private int last_price;
    @ManyToOne
    private Product product;
    public OrderItem(){

    }
    public OrderItem(int quantity, Order order, Service service, User user) {
        this.quantity = quantity;
        this.order = order;
        this.service = service;
        this.doneUser = user;
        this.userExperience = user.getExperienceModel();
        this.soldPrice = service.getPrice();
        this.servicePercentage = service.getPercentage();
    }
    public OrderItem(int quantity, Order order, Product product, User user, int last_price) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
        this.doneUser = user;
        this.last_price = last_price;
        this.soldPrice = product.getPrice();
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

    public User getDoneUser() {
        return doneUser;
    }

    public void setDoneUser(User doneUser) {
        this.doneUser = doneUser;
    }

    public int getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(int soldPrice) {
        this.soldPrice = soldPrice;
    }

    public int getServicePercentage() {
        return servicePercentage;
    }

    public void setServicePercentage(int servicePercentage) {
        this.servicePercentage = servicePercentage;
    }

    public ExperienceModel getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(ExperienceModel userExperience) {
        this.userExperience = userExperience;
    }

    public int getLast_price() {
        return last_price;
    }

    public void setLast_price(int last_price) {
        this.last_price = last_price;
    }
}
