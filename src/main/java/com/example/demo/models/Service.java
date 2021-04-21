package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(generator = "sequence-service-generator")
    @GenericGenerator(
            name = "sequence-service-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "service_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "100"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long Id;
    private String service_name;
    private String service_description;
    private int percentage;
    private int price;
    public Service(){
    }
    public Service(String service_name, String service_description, int price,int percentage) {
        this.service_name = service_name;
        this.service_description = service_description;
        this.price = price;
        this.percentage = percentage;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_description() {
        return service_description;
    }

    public void setService_description(String service_description) {
        this.service_description = service_description;
    }

    public int getPrice() {
        return price;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int procent) {
        this.percentage = procent;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
