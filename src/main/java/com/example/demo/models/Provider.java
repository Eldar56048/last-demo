package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Provider {
    @Id
    @GeneratedValue(generator = "sequence-provider-generator")
    @GenericGenerator(
            name = "sequence-provider-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "provider_sequence"),
                    @Parameter(name = "initial_value", value = "100"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private String provider_name,provider_address,provider_telephone;
    public Provider(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Provider(String provider_name, String provider_address, String provider_telephone) {
        this.provider_name = provider_name;
        this.provider_address = provider_address;
        this.provider_telephone = provider_telephone;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProvider_address() {
        return provider_address;
    }

    public void setProvider_address(String provider_address) {
        this.provider_address = provider_address;
    }

    public String getProvider_telephone() {
        return provider_telephone;
    }

    public void setProvider_telephone(String provider_telephone) {
        this.provider_telephone = provider_telephone;
    }
}
