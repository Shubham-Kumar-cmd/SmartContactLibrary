package com.smart.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;

    @NotBlank(message="name can not be empty!!")
    @Size(min=3,max=32,message="name must be between 3 - 32 characters ")
    private String name;

    @Column(unique = true)
    @Pattern(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$" ,message="invalid email")
    private String email;

    @NotBlank(message="password can not be empty!!")
    //@Size(min=8,max=32,message="password must be at least one digit,at least one lowercase letter,contain one uppercase letter,at least one special character")
    private String password;
    private String image;

    @Column(length = 500)
    private String about;
    private String role;
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "user",fetch= FetchType.LAZY)
    private List<Contact> contacts;
    // =List.of();
}



//password design pattern
/*
^                                            Match the beginning of the string
(?=.*[0-9])                                  Require that at least one digit appear anywhere in the string
(?=.*[a-z])                                  Require that at least one lowercase letter appear anywhere in the string
(?=.*[A-Z])                                  Require that at least one uppercase letter appear anywhere in the string
(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\])        Require that at least one special character appear anywhere in the string
.{8,32}                                      The password must be at least 8 characters long, but no more than 32
$
 */     
