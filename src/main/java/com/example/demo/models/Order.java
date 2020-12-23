package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(generator = "sequence-order-generator")
    @GenericGenerator(
            name = "sequence-order-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "order_sequence"),
                    @Parameter(name = "initial_value", value = "100"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "number")
    private String number;
    private String problem;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_type")
    private Type type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_type")
    private Model model;
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
    private Boolean notified;
    public Order(){}
    public Order(String name, String number, String problem, User acceptedUser, Type type, Model model) {
        this.name = name;
        this.number = number;
        this.problem = problem;
        this.acceptedUser = acceptedUser;
        this.accepted_date = new java.util.Date();
        this.status = Status.NOTDONE;
        this.type = type;
        this.model = model;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public String getName() {
        return name;
    }

    public void setName(String client_name) {
        this.name = client_name;
    }

    public String getNumber() {
        return number;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setNumber(String client_number) {
        this.number = client_number;
    }

    public String getProblem() {
        return problem;
    }
    public String getDateByFormat(){
        SimpleDateFormat ft = new SimpleDateFormat ("E, dd MMM yyyy HH:mm:ss");
        return ft.format(this.accepted_date);
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

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }
}
