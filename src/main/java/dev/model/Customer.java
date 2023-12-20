package dev.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    private String email;

    @Column(name = "pwd")
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_customer", referencedColumnName = "id")
    private List<Rol> roles;

}
