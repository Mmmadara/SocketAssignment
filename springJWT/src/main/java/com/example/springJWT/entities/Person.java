package com.example.springJWT.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userID;

    private String username;

    private String password;

    @ManyToMany
    private List<Person> friends;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Request> requests;
}
