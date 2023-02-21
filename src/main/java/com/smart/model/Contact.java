package com.smart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int contact_id;
    private String name;
    private String nickName;
    private long phone;

    @Column(unique = true)
    private String email;

    @Column(length = 7000)
    private String description;
    private String image;
    private String work;

    @ManyToOne
    private User user;
    
    @Override
    public boolean equals(Object obj) {
    	return this.contact_id==((Contact)obj).getContact_id();
    }
}
