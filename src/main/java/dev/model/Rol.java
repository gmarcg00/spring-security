package dev.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigInteger;

@Entity
@Table(name = "roles")
@Getter
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "role_name")
    private String name;

    private String description;
}
