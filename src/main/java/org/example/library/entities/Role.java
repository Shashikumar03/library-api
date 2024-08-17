package org.example.library.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private int id;

    @Column(name = "name")
    private String name;

//    @ManyToOne(cascade = CascadeType.ALL)
//    private Set<Role> roles ;




}