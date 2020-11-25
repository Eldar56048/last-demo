package com.example.demo.models;

import com.example.demo.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String client_name,client_number,problem;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accepted_user_id")
    private User acceptedUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "done_user_id")
    private User doneUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "give_user_id")
    private User giveUser;
    private java.util.Date accepted_date,gave_date;
    private int price;
    private Status status;
    public Order(){}
    public Order(String client_name, String client_number, String problem, User acceptedUser) {
        this.client_name = client_name;
        this.client_number = client_number;
        this.problem = problem;
        this.acceptedUser = acceptedUser;
        this.accepted_date = new java.util.Date();
        this.status = Status.NOTDONE;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    public void addOrderItem(OrderItem orderItem){
        this.items.add(orderItem);
        if(orderItem.getProduct()!=null) {
            this.price += orderItem.getQuantity() * orderItem.getProduct().getPrice();
        }
        else {
            this.price+=orderItem.getQuantity()*orderItem.getService().getPrice();
        }
    }
    public OrderItem getOrderItemById(long id){
        for(OrderItem orderItem:items){
            if(orderItem.getId()==id){
                return orderItem;
            }
        }
        return null;
    }
    public void removeOrderItemById(long id){
        for(OrderItem orderItem:items){
            if(orderItem.getId()==id){
                if(orderItem.getProduct()!=null) {
                    this.price -= orderItem.getQuantity() * orderItem.getProduct().getPrice();
                    items.remove(orderItem);
                }
                else {
                    this.price-=orderItem.getQuantity()*orderItem.getService().getPrice();
                    items.remove(orderItem);
                }
                break;
            }
        }
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getAcceptedUser() {
        return acceptedUser;
    }

    public void setAcceptedUser(User acceptedUser) {
        this.acceptedUser = acceptedUser;
    }

    public User getDoneUser() {
        return doneUser;
    }

    public void setDoneUser(User doneUser) {
        this.doneUser = doneUser;
    }

    public User getGiveUser() {
        return giveUser;
    }

    public void setGiveUser(User giveUser) {
        this.giveUser = giveUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_number() {
        return client_number;
    }

    public void setClient_number(String client_number) {
        this.client_number = client_number;
    }

    public String getProblem() {
        return problem;
    }
    public String getDateByFormat(){
        SimpleDateFormat ft = new SimpleDateFormat ("E, MMM dd yyyy");
        return ft.format(this.gave_date);
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Date getAccepted_date() {
        return accepted_date;
    }

    public void setAccepted_date(Date accepted_date) {
        this.accepted_date = accepted_date;
    }

    public Date getGave_date() {
        return gave_date;
    }

    public void setGave_date(Date gave_date) {
        this.gave_date = gave_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
