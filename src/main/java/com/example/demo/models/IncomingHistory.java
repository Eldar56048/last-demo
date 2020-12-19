package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import java.util.Date;

@Entity
@Table(name = "incoming_history")
public class IncomingHistory {
    @Id
    @GeneratedValue(generator = "sequence-incoming-history-generator")
    @GenericGenerator(
            name = "sequence-incoming-history-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "incoming_sequence"),
                    @Parameter(name = "initial_value", value = "100"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Provider provider;
    private int quantity;
    private int price;
    private int total;
    private Date date;
    public IncomingHistory(){}

    public IncomingHistory(Product product, Provider provider, int quantity, int price) {
        this.product = product;
        this.provider = provider;
        this.quantity = quantity;
        this.price = price;
        this.total = price*quantity;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }
    public String getDateByFormat(){
        SimpleDateFormat ft = new SimpleDateFormat ("E, MMM dd yyyy");
        return ft.format(this.date);
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
